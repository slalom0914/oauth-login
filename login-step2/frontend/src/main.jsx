import { createRoot } from 'react-dom/client'
import App from './App.jsx'
import { BrowserRouter } from 'react-router-dom'
import JoinPage from './components/pages/JoinPage.jsx'
import AuthProvider from './AuthProvider.jsx'

createRoot(document.getElementById('root')).render(
  <>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginPage />} />
        <Route path="/joinForm" element={<JoinPage />} />
        <Route path="/oauth/google/redirect" element={<GoogleRedirect />} />
        <Route path="/oauth/kakao/redirect" element={<KakaoRedirect />} />
        <Route path="/*" element={
          <AuthProvider>
            <App />
          </AuthProvider>
        }/>
      </Routes>
    </BrowserRouter>
  </>
)
