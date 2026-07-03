import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  username = '';
  password = '';
  errorMessage = '';
  isLoading = false;

  constructor(private router: Router) {}

  onSubmit(): void {
    this.errorMessage = '';
    this.isLoading = true;

    // --- Hardcoded authentication (as per assessment requirement) ---
    // TODO: Replace with HTTP POST to Spring Boot backend:
    //
    // this.http.post('/api/auth/login', { username: this.username, password: this.password })
    //   .subscribe({
    //     next: (response: any) => {
    //       localStorage.setItem('token', response.token);
    //       this.router.navigate(['/dashboard']);
    //     },
    //     error: () => {
    //       this.errorMessage = 'Invalid username or password.';
    //       this.isLoading = false;
    //     }
    //   });

    // Simulate a small network delay for a realistic feel
    setTimeout(() => {
      if (this.username === 'admin' && this.password === 'admin123') {
        // Store login state so the navbar and app know the user is authenticated
        localStorage.setItem('isLoggedIn', 'true');
        this.router.navigate(['/dashboard']);
      } else {
        this.errorMessage = 'Invalid username or password.';
      }
      this.isLoading = false;
    }, 500);
  }
}
