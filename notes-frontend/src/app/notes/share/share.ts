import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NotesService } from '../notes';

@Component({
  selector: 'app-share',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './share.html',
  styleUrls: ['./share.scss']
})
export class ShareComponent implements OnInit {
  shareForm!: FormGroup;
  noteId: number | null = null;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private notesService: NotesService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    // 1. Obtener el ID de la nota desde la URL
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.noteId = +id;
      } else {
        this.router.navigate(['/notes']);
      }
    });

    // 2. Definición del formulario
    this.shareForm = this.fb.group({
      recipientEmail: ['', [Validators.required, Validators.email]]
    });
  }

  onSubmit(): void {
    this.successMessage = null;
    this.errorMessage = null;

    if (this.shareForm.valid && this.noteId) {
      const recipientEmail = this.shareForm.value.recipientEmail;

      // 3. Llamar al servicio de compartir
      this.notesService.shareNote(this.noteId, recipientEmail).subscribe({
        next: () => {
          this.successMessage = `Nota compartida exitosamente con ${recipientEmail}.`;
          this.shareForm.reset();
        },
        error: (err: any) => {
          // Muestra errores de backend (ej. "El destinatario no existe" o "Solo el dueño puede compartir")
          this.errorMessage = err.error || 'Error al compartir la nota.';
        }
      });
    }
  }

  // Método para volver a la lista de notas
  onCancel(): void {
    this.router.navigate(['/notes']);
  }
}