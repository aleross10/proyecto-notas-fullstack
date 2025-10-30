import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { NotesService } from '../notes'; 
import { AuthService } from '../../auth/auth'; 
import { NoteDto } from '../../auth/interfaces'; 

@Component({
  selector: 'app-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form.html',
  styleUrls: ['./form.scss'] 
})
export class FormComponent implements OnInit {
  noteForm!: FormGroup;
  isEditMode = false;
  noteId: number | null = null;
  errorMessage: string | null = null;
  currentEmail: string | null = null;

  constructor(
    private fb: FormBuilder,
    private notesService: NotesService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute 
  ) {}

  ngOnInit(): void {
    this.noteForm = this.fb.group({
      title: ['', Validators.required],
      content: ['', Validators.required],
      isPublic: [false]
    });

    this.currentEmail = this.authService.getCurrentUserEmail();

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.noteId = +id; 
        this.loadNoteData(this.noteId);
      }
    });
  }

  loadNoteData(id: number): void {
    this.notesService.getNoteById(id).subscribe({
      next: (note) => {
        if (note.ownerEmail !== this.currentEmail) {
          this.errorMessage = 'No tienes permiso para editar esta nota.';
          this.noteForm.disable();
        } else {
          this.noteForm.patchValue(note);
        }
      },
      error: () => {
        this.errorMessage = 'No se pudo cargar la nota.';
        this.router.navigate(['/notes']);
      }
    });
  }

  onSubmit(): void {
    if (this.noteForm.invalid) {
      return;
    }

    const noteData = this.noteForm.value;
    
    if (this.isEditMode && this.noteId) {
      const noteToUpdate: NoteDto = { 
        id: this.noteId, 
        title: noteData.title, 
        content: noteData.content, 
        isPublic: noteData.isPublic,
        ownerEmail: this.currentEmail! 
      };
      
      this.notesService.updateNote(noteToUpdate).subscribe({
        next: () => this.router.navigate(['/notes']),
        error: (err) => this.errorMessage = 'Error al actualizar la nota.'
      });

    } else {
      this.notesService.createNote(noteData).subscribe({
        next: () => this.router.navigate(['/notes']),
        error: (err) => this.errorMessage = 'Error al crear la nota.'
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/notes']);
  }
}