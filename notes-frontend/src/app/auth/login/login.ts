import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms'; 
import { Router, RouterModule } from '@angular/router'; 


import { AuthService } from '../auth'; 

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule], 
  templateUrl: './login.html', 
  styleUrls: ['./login.scss']
})
export class LoginComponent implements OnInit {

  loginForm!: FormGroup; 
  errorMessage: string | null = null; 

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required] 
    });
  }

  onSubmit(): void {
    this.errorMessage = null; 
    if (this.loginForm.valid) {
      
      // El AuthService ya guarda el token.
      this.authService.login(this.loginForm.value).subscribe({
        
        next: () => { 
          this.router.navigate(['/notes']); // Redirige a las notas
        },
        
        error: (err: any) => {
          this.errorMessage = err.error?.message || err.error || 'Credenciales inválidas.';
        }
      });
    }
  }
}