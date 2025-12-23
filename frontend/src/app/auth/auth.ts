import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common'; // 必须引入
import { FormsModule } from '@angular/forms';     // 必须引入
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

// 引入你的 AuthService
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  // 务必确保 imports 包含 CommonModule 和 FormsModule
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './auth.html',
  styleUrls: ['./auth.css']
})
export class AuthComponent {
  isRegister: boolean = false;
  errorMessage: string = '';
  showError: boolean = false;

  // 登录对象
  loginObj: any = {
    email: '',
    password: ''
  };

  // 注册对象 (前端模型)
  registerObj: any = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    affiliation: '',
    country: ''
  };

  constructor(
    private router: Router,
    private http: HttpClient,
    private cd: ChangeDetectorRef,
    private authService: AuthService
  ) {}

  // --- 登录逻辑 ---
  onLogin(): void {
    // 简单的非空验证
    if (!this.loginObj.email || !this.loginObj.password) {
      this.errorMessage = 'Please fill in all fields.';
      this.triggerError();
      return;
    }

    this.showError = false;
    this.http.post('http://localhost:8080/api/auth/login', this.loginObj).subscribe({
      next: (res: any) => {
        // 登录成功，调用 AuthService 保存状态
        this.authService.loginSuccess(res);
        // Redirect based on role
        if (res.role === 'Admin') {
          this.router.navigate(['/conference']);
        } else if (res.role === 'Reviewer') {
          this.router.navigate(['/review-execution']);
        } else if (res.role === 'Chair') {
          this.router.navigate(['/conference']);
        } else {
          this.router.navigate(['/paper-submission']);
        }
      },
      error: (err) => {
        if (err.status === 401) {
          this.errorMessage = 'Invalid email or password';
        } else {
          this.errorMessage = 'Server connection failed (Is Backend running?)';
        }
        this.triggerError();
      }
    });
  }

  // --- 注册逻辑 ---
  onRegister(): void {
    // 简单的非空验证
    if (!this.registerObj.email || !this.registerObj.password || !this.registerObj.firstName) {
      this.errorMessage = 'Please fill in required fields.';
      this.triggerError();
      return;
    }

    this.showError = false;

    // 1. 数据转换：前端驼峰 -> 后端下划线格式
    // 这是保证后端不报错的关键步骤
    const dbUser = {
      first_name: this.registerObj.firstName,
      last_name: this.registerObj.lastName,
      email: this.registerObj.email,
      password_hash: this.registerObj.password, // 注意：后端通过 password_hash 接收
      affiliation: this.registerObj.affiliation,
      country: this.registerObj.country,
      role: 'User',
      is_email_verified: false
    };

    // 2. 发送请求到 /users
    this.http.post('http://localhost:8080/api/users', dbUser).subscribe({
      next: (res: any) => {
        alert('Registration Successful! Please Sign In.');

        // 重置表单
        this.registerObj = {
          firstName: '', lastName: '', email: '',
          password: '', affiliation: '', country: ''
        };

        // 切换回登录视图
        this.isRegister = false;
        this.cd.detectChanges();
      },
      error: (err) => {
        console.error("Registration Error:", err);
        // 如果后端返回了具体的错误信息 (message)，则显示它，否则显示通用错误
        this.errorMessage = err.error?.message || 'Registration Failed. Email might be taken.';
        this.triggerError();
      }
    });
  }

  // 辅助方法：控制错误提示显示时间
  private triggerError() {
    this.showError = true;
    this.cd.detectChanges();
    setTimeout(() => {
      this.showError = false;
      this.cd.detectChanges();
    }, 4000);
  }
}
