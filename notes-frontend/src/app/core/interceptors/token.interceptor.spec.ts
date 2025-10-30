import { TestBed } from '@angular/core/testing';
import { HttpInterceptorFn, HttpClientModule } from '@angular/common/http';


import { tokenInterceptor } from './token.interceptor'; 

describe('tokenInterceptor', () => {
  const interceptor: HttpInterceptorFn = (req, next) => 
    TestBed.runInInjectionContext(() => tokenInterceptor(req, next));

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientModule] 
    });
  });

  it('should be created', () => {
    expect(interceptor).toBeTruthy();
  });
});