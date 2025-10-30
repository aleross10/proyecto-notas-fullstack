import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';

// 1. CORRECCIÓN: Ruta al servicio actualizada
import { AuthService } from '../auth'; 

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RouterModule], 
  templateUrl: './register.html',
  styleUrls: ['./register.scss']
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup; 
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.registerForm.valid) {
      this.authService.register(this.registerForm.value).subscribe({
        
        next: (response: any) => { 
          this.successMessage = '¡Registro exitoso! Redirigiendo al login...';
          this.registerForm.reset();
          
          setTimeout(() => {
            this.router.navigate(['/login']); 
          }, 2000);
        },
        
        error: (err: any) => {
          this.errorMessage = err.error?.message || err.error || 'Error al registrar. El email ya puede estar en uso.';
          console.error('Error en el registro:', err);
        }
      });
    }
  }
}