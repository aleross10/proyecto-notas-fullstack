
export interface LoginResponseDto {
  jwt: string;
}


export interface NoteDto {
  id: number;
  title: string;
  content: string;
  isPublic: boolean;
  ownerEmail: string;
}