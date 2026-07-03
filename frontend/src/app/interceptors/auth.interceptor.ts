import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Basic Authentication: admin/admin123 (hardcoded as per assessment spec)
  const credentials = btoa('admin:admin123');
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Basic ${credentials}`
    }
  });
  return next(authReq);
};
