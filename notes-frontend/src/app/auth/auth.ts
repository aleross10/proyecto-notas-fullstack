import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { LoginResponseDto } from './interfaces'; 

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = '/api/auth'; // Usa la ruta relativa para el proxy
  private currentUserEmail: string | null = null; 

  constructor(private http: HttpClient, private router: Router) { 
    this.decodeAndSetCurrentUser();
  }
  
  

  register(registerData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, registerData);
  }

  login(loginData: any): Observable<LoginResponseDto> {
    return this.http.post<LoginResponseDto>(`${this.apiUrl}/login`, loginData).pipe(
      tap(response => {
        localStorage.setItem('authToken', response.jwt);
        this.decodeAndSetCurrentUser();
      })
    );
  }

  logout(): void {
    localStorage.removeItem('authToken');
    this.currentUserEmail = null;
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) {
      return false;
    }
    try {
      const decodedToken: { exp: number } = jwtDecode(token);
      const isExpired = decodedToken.exp * 1000 < Date.now();
      if (isExpired) {
        this.logout();
        return false;
      }
      return true;
    } catch (error) {
      this.logout();
      return false; 
    }
  }

  // --- Métodos de Usuario (Decodificación) ---

  getCurrentUserEmail(): string | null {
    if (!this.currentUserEmail) {
      this.decodeAndSetCurrentUser();
    }
    return this.currentUserEmail;
  }
  
  decodeAndSetCurrentUser(): void {
    const token = this.getToken();
    if (token) {
      try {
          const decodedToken: { sub: string } = jwtDecode(token);
          this.currentUserEmail = decodedToken.sub;
      } catch (error) {
          this.currentUserEmail = null;
      }
    } else {
      this.currentUserEmail = null;
    }
  }
}