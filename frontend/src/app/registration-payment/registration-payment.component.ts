import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { catchError, throwError } from 'rxjs';

interface Conference {
  conference_id: number;
  title: string;
  location: string;
  date: string;
  description?: string;
}

interface Registration {
  registration_id: number;
  conference_id: number;
  registration_type: 'student' | 'professional';
  early_bird: boolean;
  dietary_requirements: string;
  payment_status: 'pending' | 'paid' | 'cancelled';
  amount: number;
  created_at: string;
}

interface Payment {
  payment_id: number;
  registration_id: number;
  amount: number;
  receipt_file: string;
  payment_method: string;
  paid_at: string;
}

interface InvoiceDisplay {
  id: number;
  conference_name: string;
  status: 'pending' | 'paid' | 'cancelled';
  amount: number;
  receipt: string | null;
  registration_date: string;
  payment_date: string | null;
  conference_id?: number;
}

@Component({
  selector: 'app-registration-payment',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule, RouterModule],
  templateUrl: './registration-payment.component.html',
  styleUrls: ['./registration-payment.component.css']
})
export class RegistrationPaymentComponent implements OnInit {
  private readonly API_BASE_URL = 'http://localhost:8080/api';

  conferences: Conference[] = [];
  invoices: InvoiceDisplay[] = [];

  selectedConferenceId: number | string | null = null;
  registrationType: 'student' | 'professional' = 'student';
  earlyBird: boolean = false;
  dietaryRequirements: string = '';
  registrationPrice: number = 0;

  showPaymentModal: boolean = false;
  currentStep: 1 | 2 | 3 = 1;
  selectedInvoiceId: number | null = null;
  selectedInvoiceAmount: number = 0;

  receiptFile: File | null = null;
  paymentMethod: string = 'bank_transfer';

  isLoading: boolean = true;
  isProcessing: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.registrationPrice = this.registrationType === 'student' ? 100 : 250;
    this.loadAllData();
  }

  private loadAllData(): void {
    this.isLoading = true;

    this.http.get<Conference[]>(`${this.API_BASE_URL}/conferences`).subscribe({
      next: (conferences) => {
        console.log('📊 Conferences loaded:', conferences);
        this.conferences = conferences || [];
        console.log('📊 Conference IDs:', this.conferences.map(c => ({ id: c.conference_id, title: c.title })));

        this.http.get<Registration[]>(`${this.API_BASE_URL}/registrations`).subscribe({
          next: (registrations) => {
            console.log('📊 Registrations loaded:', registrations);

            this.http.get<Payment[]>(`${this.API_BASE_URL}/payments`).subscribe({
              next: (payments) => {
                console.log('📊 Payments loaded:', payments);
                this.processInvoices(registrations || [], payments || []);
                this.isLoading = false;
                this.cdr.detectChanges();
              },
              error: (error) => {
                console.error('Error loading payments:', error);
                this.processInvoices(registrations || [], []);
                this.isLoading = false;
                this.cdr.detectChanges();
              }
            });
          },
          error: (error) => {
            console.error('Error loading registrations:', error);
            this.errorMessage = 'Failed to load registrations.';
            this.isLoading = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (error) => {
        console.error('Error loading conferences:', error);
        this.errorMessage = 'Failed to load conferences.';
        this.isLoading = false;
        this.cdr.detectChanges();
      }
    });
  }

  private processInvoices(registrations: Registration[], payments: Payment[]): void {
    console.log('🔄 Processing invoices...');

    const existingInvoiceMap = new Map<number, InvoiceDisplay>();
    this.invoices.forEach(invoice => {
      existingInvoiceMap.set(invoice.id, invoice);
    });

    this.invoices = registrations.map(reg => {
      const existingInvoice = existingInvoiceMap.get(reg.registration_id);

      const conference = this.conferences.find(c => c.conference_id == reg.conference_id);

      let conferenceName = 'Unknown Conference';
      if (existingInvoice && existingInvoice.conference_name !== 'Unknown Conference') {
        conferenceName = existingInvoice.conference_name;
      } else if (conference) {
        conferenceName = conference.title;
      }

      const payment = payments.find(p => p.registration_id === reg.registration_id);

      let status: 'pending' | 'paid' | 'cancelled' = reg.payment_status;
      if (payment) {
        status = 'paid';
      }

      let amount = 0;
      if (payment?.amount) {
        amount = payment.amount;
      } else if (reg.amount) {
        amount = reg.amount;
      } else {
        const isStudent = reg.registration_type === 'student';
        const baseAmount = isStudent ? 100 : 250;
        amount = reg.early_bird ? baseAmount - 30 : baseAmount;
      }

      let registrationDate = new Date();
      let paymentDate = null;

      if (reg.created_at) {
        try {
          registrationDate = new Date(reg.created_at);
          if (isNaN(registrationDate.getTime())) {
            registrationDate = new Date();
          }
        } catch (e) {
          registrationDate = new Date();
        }
      }

      if (payment?.paid_at) {
        try {
          paymentDate = new Date(payment.paid_at);
          if (isNaN(paymentDate.getTime())) {
            paymentDate = null;
          }
        } catch (e) {
          paymentDate = null;
        }
      }

      return {
        id: reg.registration_id,
        conference_name: conferenceName,
        status: status,
        amount: amount,
        receipt: payment?.receipt_file || null,
        registration_date: this.formatDate(registrationDate),
        payment_date: paymentDate ? this.formatDate(paymentDate) : null,
        conference_id: reg.conference_id
      };
    });
  }

  private formatDate(date: Date): string {
    if (!date || isNaN(date.getTime())) {
      date = new Date();
    }

    try {
      return date.toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    } catch (e) {
      return 'Invalid Date';
    }
  }

  calculatePrice(): void {
    let basePrice = this.registrationType === 'student' ? 100 : 250;
    this.registrationPrice = this.earlyBird ? basePrice - 30 : basePrice;
  }

  onConferenceSelect(): void {
    console.log('🔍 Conference selected:', this.selectedConferenceId);
    console.log('🔍 Type:', typeof this.selectedConferenceId);

    if (this.selectedConferenceId) {
      this.calculatePrice();
    }
  }

  getSelectedConferenceDate(): string {
    if (!this.selectedConferenceId) return '';

    const conferenceId = Number(this.selectedConferenceId);
    const conference = this.conferences.find(c => c.conference_id === conferenceId);
    if (!conference?.date) return '';

    try {
      const date = new Date(conference.date);
      if (isNaN(date.getTime())) {
        return conference.date;
      }
      return this.formatDate(date);
    } catch {
      return conference.date;
    }
  }

  submitRegistration(): void {
    console.log('=== SUBMIT REGISTRATION ===');
    console.log('Selected conference ID:', this.selectedConferenceId);

    if (!this.selectedConferenceId) {
      this.showError('Please select a conference');
      return;
    }

    this.isProcessing = true;

    const conferenceId = Number(this.selectedConferenceId);
    const selectedConference = this.conferences.find(c => c.conference_id === conferenceId);

    console.log('Looking for conference ID:', conferenceId);
    console.log('Found conference:', selectedConference);

    if (!selectedConference) {
      this.showError('Conference not found. Please refresh and try again.');
      this.isProcessing = false;
      return;
    }

    const conferenceTitle = selectedConference.title;

    const registrationData = {
      conference_id: conferenceId,
      registration_type: this.registrationType,
      early_bird: this.earlyBird,
      dietary_requirements: this.dietaryRequirements,
      payment_status: 'pending',
      amount: this.registrationPrice,
      created_at: new Date().toISOString()
    };

    console.log('Sending to backend:', registrationData);

    this.http.post<Registration>(`${this.API_BASE_URL}/registrations`, registrationData)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('HTTP Error:', error);
          this.showError(`Registration failed: ${error.error?.message || error.message}`);
          this.isProcessing = false;
          return throwError(() => error);
        })
      )
      .subscribe({
        next: (response) => {
          console.log('✅ Registration successful:', response);

          const newInvoice: InvoiceDisplay = {
            id: response.registration_id,
            conference_name: conferenceTitle,
            status: 'pending',
            amount: this.registrationPrice,
            receipt: null,
            registration_date: this.formatDate(new Date()),
            payment_date: null,
            conference_id: conferenceId
          };

          this.invoices.unshift(newInvoice);
          this.selectedInvoiceId = response.registration_id;
          this.selectedInvoiceAmount = this.registrationPrice;

          this.receiptFile = null;
          this.paymentMethod = 'bank_transfer';

          this.currentStep = 2;
          this.showPaymentModal = true;
          this.isProcessing = false;
          this.showSuccess('Registration successful. Please proceed with payment.');
          this.cdr.detectChanges();
        },
        error: (error) => {
          this.isProcessing = false;
        }
      });
  }

  onReceiptSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.receiptFile = input.files[0];
    }
  }

  submitPayment(): void {
    if (!this.receiptFile) {
      this.showError('Please upload payment receipt');
      return;
    }

    if (!this.selectedInvoiceId) {
      this.showError('No registration selected');
      return;
    }

    this.isProcessing = true;

    const invoiceIndex = this.invoices.findIndex(i => i.id === this.selectedInvoiceId);
    if (invoiceIndex === -1) {
      this.showError('Invoice not found');
      this.isProcessing = false;
      return;
    }

    const invoice = this.invoices[invoiceIndex];
    const paymentAmount = invoice.amount;
    const conferenceTitle = invoice.conference_name;

    const paymentData = {
      registration_id: this.selectedInvoiceId,
      amount: paymentAmount,
      payment_method: this.paymentMethod,
      receipt_file: this.receiptFile.name,
      paid_at: new Date().toISOString()
    };

    this.http.post<Payment>(`${this.API_BASE_URL}/payments`, paymentData)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.error('Payment error:', error);
          this.showError(`Payment failed: ${error.error?.message || error.message}`);
          this.isProcessing = false;
          return throwError(() => error);
        })
      )
      .subscribe({
        next: (paymentResponse) => {
          this.http.put(`${this.API_BASE_URL}/registrations/${this.selectedInvoiceId}`, {
            payment_status: 'paid',
            amount: paymentAmount
          }).subscribe({
            next: () => {
              this.invoices[invoiceIndex].status = 'paid';
              this.invoices[invoiceIndex].receipt = this.receiptFile?.name || 'submitted';
              this.invoices[invoiceIndex].payment_date = this.formatDate(new Date());
              this.invoices[invoiceIndex].conference_name = conferenceTitle;

              this.currentStep = 3;
              this.isProcessing = false;
              this.showSuccess('Payment submitted successfully!');
              this.cdr.detectChanges();
            },
            error: (error) => {
              console.error('Status update failed:', error);
              this.showError('Payment recorded but status update failed.');
              this.isProcessing = false;
            }
          });
        },
        error: (error) => {
          this.isProcessing = false;
        }
      });
  }

  cancelRegistration(invoiceId: number): void {
    if (!confirm('Are you sure you want to cancel this registration?')) {
      return;
    }

    this.isProcessing = true;

    this.http.delete(`${this.API_BASE_URL}/registrations/${invoiceId}`).subscribe({
      next: () => {
        this.invoices = this.invoices.filter(i => i.id !== invoiceId);
        this.isProcessing = false;
        this.showSuccess('Registration cancelled successfully.');
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Cancellation failed:', error);
        this.showError('Failed to cancel registration. Please try again.');
        this.isProcessing = false;
      }
    });
  }

  closePaymentModal(): void {
    this.showPaymentModal = false;
    this.currentStep = 1;
    this.receiptFile = null;
    this.selectedInvoiceId = null;
    this.selectedInvoiceAmount = 0;
    this.resetForm();
  }

  downloadReceipt(receiptFileName: string): void {
    if (receiptFileName === 'submitted' || receiptFileName) {
      const invoice = this.invoices.find(i => i.id === this.selectedInvoiceId);
      if (!invoice) return;

      const receiptContent = `
        Conference Registration Receipt
        ===============================
        Registration ID: #${invoice.id}
        Conference: ${invoice.conference_name}
        Amount: ${this.formatCurrency(invoice.amount)}
        Status: ${invoice.status}
        Payment Date: ${invoice.payment_date || 'N/A'}
        Registration Date: ${invoice.registration_date}

        Thank you for your registration!
      `;

      const blob = new Blob([receiptContent], { type: 'text/plain' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `receipt-${invoice.id}.txt`;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);

      this.showSuccess('Receipt download started.');
    }
  }

  getStatusBadgeClass(status: string): string {
    switch (status) {
      case 'paid':
        return 'status-badge status-paid';
      case 'pending':
        return 'status-badge status-pending';
      case 'cancelled':
        return 'status-badge status-cancelled';
      default:
        return 'status-badge';
    }
  }

  getStatusText(status: string): string {
    switch (status) {
      case 'paid':
        return 'Paid';
      case 'pending':
        return 'Pending Payment';
      case 'cancelled':
        return 'Cancelled';
      default:
        return status;
    }
  }

  formatCurrency(amount: number | undefined | null): string {
    if (amount === null || amount === undefined || isNaN(amount) || amount === 0) {
      return 'RM 0.00';
    }
    return `RM ${amount.toFixed(2)}`;
  }

  getPaidTotal(): number {
    return this.invoices
      .filter(i => i.status === 'paid')
      .reduce((sum, i) => sum + (i.amount || 0), 0);
  }

  getPendingTotal(): number {
    return this.invoices
      .filter(i => i.status === 'pending')
      .reduce((sum, i) => sum + (i.amount || 0), 0);
  }

  private showError(message: string): void {
    this.errorMessage = message;
    setTimeout(() => this.errorMessage = '', 5000);
  }

  private showSuccess(message: string): void {
    this.successMessage = message;
    setTimeout(() => this.successMessage = '', 5000);
  }

  resetForm(): void {
    this.selectedConferenceId = null;
    this.registrationType = 'student';
    this.earlyBird = false;
    this.dietaryRequirements = '';
    this.calculatePrice();
  }

  getCurrentDate(): string {
    return this.formatDate(new Date());
  }

  payNow(invoice: InvoiceDisplay): void {
    this.selectedInvoiceId = invoice.id;
    this.selectedInvoiceAmount = invoice.amount || 100;
    this.showPaymentModal = true;
    this.currentStep = 2;
    this.receiptFile = null;
    this.paymentMethod = 'bank_transfer';
  }

  getSelectedInvoiceConferenceName(): string {
    if (!this.selectedInvoiceId) return '';
    const invoice = this.invoices.find(i => i.id === this.selectedInvoiceId);
    return invoice ? invoice.conference_name : '';
  }

  getSelectedInvoiceAmount(): number {
    if (!this.selectedInvoiceId) return 0;
    const invoice = this.invoices.find(i => i.id === this.selectedInvoiceId);
    return invoice ? invoice.amount : 0;
  }

  testConferenceData(): void {
    console.log('=== CONFERENCE DATA TEST ===');
    console.log('selectedConferenceId:', this.selectedConferenceId);
    console.log('Type:', typeof this.selectedConferenceId);
    console.log('All conferences:', this.conferences);
    console.log('Conference IDs:', this.conferences.map(c => ({
      id: c.conference_id,
      title: c.title,
      type: typeof c.conference_id
    })));

    if (this.selectedConferenceId) {
      const conferenceId = Number(this.selectedConferenceId);
      const found = this.conferences.find(c => c.conference_id === conferenceId);
      console.log('Found conference for ID', conferenceId, ':', found);
    }
  }

  debugDates(): void {
    console.log('=== DATE DEBUG INFO ===');
    console.log('Current invoices:', this.invoices);

    this.invoices.forEach((invoice, index) => {
      console.log(`Invoice #${invoice.id}:`);
      console.log(`  Conference: ${invoice.conference_name}`);
      console.log(`  Registration date: ${invoice.registration_date}`);
      console.log(`  Payment date: ${invoice.payment_date}`);
    });
  }
}
