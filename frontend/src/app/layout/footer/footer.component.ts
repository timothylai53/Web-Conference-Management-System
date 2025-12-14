import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule],
  template: `
    <footer class="app-footer">
      <div class="footer-container">

        <div class="footer-col brand-col">
          <div class="footer-logo">
            <img src="/assets/logo.png" alt="ScholarSync" class="footer-logo-img">
            ScholarSync
          </div>
          <p class="footer-desc">
            Streamlining academic conference management with precision,
            elegance, and efficiency for organizers and authors worldwide.
          </p>
          <div class="social-links">
            <a href="#" class="social-link" aria-label="Twitter">𝕏</a>
            <a href="#" class="social-link" aria-label="LinkedIn">in</a>
            <a href="#" class="social-link" aria-label="GitHub">git</a>
          </div>
        </div>

        <div class="footer-col">
          <h4 class="col-title">Platform</h4>
          <ul class="link-list">
            <li><a href="#">Find Conferences</a></li>
            <li><a href="#">Submit Abstract</a></li>
            <li><a href="#">Peer Review Process</a></li>
            <li><a href="#">Pricing & Plans</a></li>
          </ul>
        </div>

        <div class="footer-col">
          <h4 class="col-title">Resources</h4>
          <ul class="link-list">
            <li><a href="#">Help Center</a></li>
            <li><a href="#">API Documentation</a></li>
            <li><a href="#">Community Guidelines</a></li>
            <li><a href="#">Chair Handbook</a></li>
          </ul>
        </div>

        <div class="footer-col">
          <h4 class="col-title">Legal</h4>
          <ul class="link-list">
            <li><a href="#">Privacy Policy</a></li>
            <li><a href="#">Terms of Service</a></li>
            <li><a href="#">Cookie Policy</a></li>
            <li><a href="#">Contact Support</a></li>
          </ul>
        </div>

      </div>

      <div class="footer-bottom">
        <p>&copy; 2025 ConfEase System. All rights reserved.</p>
        <div class="meta-links">
          <span>System Status: <span class="status-ok">● Operational</span></span>
        </div>
      </div>
    </footer>
  `,
  styles: [`
    :host {
      display: block;
      margin-top: auto;
    }

    .app-footer {
      background-color: #0a1628; /* --primary-navy */
      color: #94a3b8; /* --text-light */
      font-size: 0.9rem;
      padding-top: 60px;
      border-top: 1px solid rgba(255,255,255,0.05);
    }

    .footer-container {
      max-width: 1200px;
      margin: 0 auto;
      padding: 0 40px 60px 40px;
      display: grid;
      grid-template-columns: 2fr 1fr 1fr 1fr;
      gap: 40px;
    }

    /* Brand Column */
    .brand-col {
      padding-right: 40px;
    }

    .footer-logo {
      color: white;
      font-size: 1.5rem;
      font-weight: 700;
      margin-bottom: 16px;
      display: inline-flex;
      align-items: center;
      gap: 12px; /* Increased gap for the image */
    }

    /* NEW: Style for the footer logo image */
    .footer-logo-img {
      height: 32px;
      width: auto;
      border-radius: 6px;
    }

    .footer-desc {
      line-height: 1.6;
      margin-bottom: 24px;
      color: #64748b; /* --text-secondary */
    }

    /* Social Links */
    .social-links {
      display: flex;
      gap: 12px;
    }

    .social-link {
      width: 36px;
      height: 36px;
      background: rgba(255,255,255,0.05);
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: white;
      text-decoration: none;
      transition: all 0.2s;
      border: 1px solid transparent;
    }

    .social-link:hover {
      background: #2563eb;
      border-color: rgba(255,255,255,0.2);
      transform: translateY(-2px);
    }

    /* Link Columns */
    .col-title {
      color: white;
      font-size: 1rem;
      font-weight: 600;
      margin-bottom: 20px;
      letter-spacing: -0.01em;
    }

    .link-list {
      list-style: none;
      padding: 0;
      margin: 0;
    }

    .link-list li {
      margin-bottom: 12px;
    }

    .link-list a {
      color: #94a3b8;
      text-decoration: none;
      transition: color 0.2s;
    }

    .link-list a:hover {
      color: #60a5fa; /* Light Blue */
      padding-left: 4px; /* Slight slide effect */
    }

    /* Bottom Bar */
    .footer-bottom {
      border-top: 1px solid rgba(255,255,255,0.05);
      background: #060e1a; /* Even darker shade */
      padding: 24px 40px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      font-size: 0.85rem;
    }

    .status-ok {
      color: #10b981; /* --success-green */
      font-weight: 600;
    }

    /* Responsive */
    @media (max-width: 900px) {
      .footer-container {
        grid-template-columns: 1fr 1fr;
      }
      .brand-col { grid-column: span 2; padding-right: 0; }
    }

    @media (max-width: 600px) {
      .footer-container { display: flex; flex-direction: column; }
      .footer-bottom { flex-direction: column; gap: 12px; text-align: center; }
    }
  `]
})
export class FooterComponent {}
