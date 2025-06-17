import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService, UserDto, RegisterDto } from '../../services/auth.service';
import { ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AuthComponent {
  isLoginMode: boolean = true;
  loginData: UserDto = { username: '', password: '' };
  registerData: RegisterDto = { username: '', password: '', whatsapp: '' };
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  toggleMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.errorMessage = '';
  }

  onSubmit(): void {
    if (this.isLoginMode) {
      this.authService.login(this.loginData).subscribe({
        next: () => this.router.navigate(['/clientes']),
        error: () => this.errorMessage = 'Usuario o contraseña incorrectos.'
      });
    } else {
      this.authService.register(this.registerData).subscribe({
        next: () => {
          this.errorMessage = '';
          this.isLoginMode = true;
          alert('Usuario registrado con éxito. Ahora puedes iniciar sesión.');
        },
        error: () => this.errorMessage = 'Hubo un error al registrar el usuario.'
      });
    }
  }

  showPasswordLogin: boolean = false;
  showPasswordRegister: boolean = false;

  togglePasswordVisibility(tipo: 'login' | 'register'): void {
    if (tipo === 'login') {
      this.showPasswordLogin = !this.showPasswordLogin;
    } else {
      this.showPasswordRegister = !this.showPasswordRegister;
    }
  }
}
