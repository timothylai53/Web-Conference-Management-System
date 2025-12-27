import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

// 引入 AuthService
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.html',
  styleUrls: ['./user-profile.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule]
})
export class UserProfileComponent implements OnInit {
  // --- 关键修改：显式添加 public 关键字 ---
  // 这将解决 "Private member is not accessible" 错误，让 HTML 能够访问它们
  public profileForm!: FormGroup;
  public currentUser: any;
  public showSuccess: boolean = false; // 控制成功消息显示

  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private cd: ChangeDetectorRef,
    public authService: AuthService // 建议设为 public，以防 HTML 需要直接调用
  ) {}

  ngOnInit(): void {
    const user = localStorage.getItem('currentUser');
    if (user) {
      this.currentUser = JSON.parse(user);
      this.initForm();
    }
  }

  // 修改为 public 方法
  public initForm(): void {
    // 表单初始化逻辑，与 HTML 中的 formControlName 对应
    this.profileForm = this.fb.group({
      first_name: [this.currentUser?.first_name || '', Validators.required],
      last_name: [this.currentUser?.last_name || '', Validators.required],
      email: [this.currentUser?.email || '', [Validators.required, Validators.email]],
      affiliation: [this.currentUser?.affiliation || ''],
      country: [this.currentUser?.country || ''],
      orcid: [this.currentUser?.orcid || '']
    });
  }

  // 修改为 public 方法
  public onUpdate(): void {
    if (this.profileForm.invalid) return;

    const updatedData = {
      ...this.currentUser,
      ...this.profileForm.value
    };

    // 保持原本的 PUT 请求逻辑
    this.http.put(`http://host.docker.internal:8080/api/users/${this.currentUser.user_id}`, updatedData).subscribe({
      next: (res: any) => {
        // 1. 通知 AuthService 更新全局状态
        this.authService.updateUser(res);

        // 2. 更新本地视图数据
        this.currentUser = res;

        // 3. 触发成功弹窗
        this.showSuccess = true;
        this.cd.detectChanges(); // 强制刷新视图，确保弹窗立即显示

        // 4. 2.5秒后自动隐藏弹窗
        setTimeout(() => {
          this.showSuccess = false;
          this.cd.detectChanges();
        }, 2500);
      },
      error: (err) => {
        console.error('Update failed', err);
        alert('Update failed: ' + (err.error?.message || 'Check connection'));
      }
    });
  }
}
