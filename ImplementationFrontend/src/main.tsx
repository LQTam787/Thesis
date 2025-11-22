import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { SetupProvider } from './store/setup-context.tsx'
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext'; // Import AuthProvider

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <SetupProvider>
      <BrowserRouter>
        <AuthProvider> {/* Wrap App with AuthProvider */}
          <App />
        </AuthProvider>
      </BrowserRouter>
    </SetupProvider>
  </StrictMode>,
)
