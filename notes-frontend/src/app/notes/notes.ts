import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NoteDto } from '../auth/interfaces'; 

@Injectable({
  providedIn: 'root'
})
export class NotesService {

  private apiUrl = '/api/notes'; 

  constructor(private http: HttpClient) { }

  getAllNotes(): Observable<NoteDto[]> {
    return this.http.get<NoteDto[]>(this.apiUrl);
  }

  getNoteById(id: number): Observable<NoteDto> {
    return this.http.get<NoteDto>(`${this.apiUrl}/${id}`);
  }

  createNote(note: { title: string, content: string, isPublic: boolean }): Observable<NoteDto> {
    return this.http.post<NoteDto>(this.apiUrl, note);
  }

  updateNote(note: NoteDto): Observable<NoteDto> {
    return this.http.put<NoteDto>(`${this.apiUrl}/${note.id}`, note);
  }

  deleteNote(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  shareNote(id: number, recipientEmail: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/share`, { recipientEmail });
  }
}