import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { SetupProvider } from './store/setup-context.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <SetupProvider>
      <App />
    </SetupProvider>
  </StrictMode>,
)
