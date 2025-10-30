import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router'; 
import { NotesService } from '../notes';      
import { AuthService } from '../../auth/auth';         
import { NoteDto } from '../../auth/interfaces';        
import { Observable } from 'rxjs';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [CommonModule, RouterLink], 
  templateUrl: './list.html',
  styleUrls: ['./list.scss']
})
export class ListComponent implements OnInit {

  notes$!: Observable<NoteDto[]>;
  errorMessage: string | null = null;
  currentEmail: string | null = null;

  constructor(
    private notesService: NotesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.currentEmail = this.authService.getCurrentUserEmail();
    this.loadNotes();
  }

  loadNotes(): void {
    this.errorMessage = null;
    this.notes$ = this.notesService.getAllNotes();

    this.notes$.subscribe({
      error: err => {
        this.errorMessage = 'Error al cargar las notas. ¿Has iniciado sesión?';
        console.error(err);
        if (err.status === 401 || err.status === 403) {
          this.authService.logout(); 
        }
      }
    });
  }

 
  deleteNote(id: number): void {
    if (confirm('¿Estás seguro de que quieres eliminar esta nota?')) {
      this.notesService.deleteNote(id).subscribe({
        next: () => {
          this.loadNotes(); 
        },
        error: err => {
          this.errorMessage = 'Error al eliminar la nota. Es posible que no seas el dueño.';
          console.error(err);
        }
      });
    }
  }

  logout(): void {
    this.authService.logout();
  }
}