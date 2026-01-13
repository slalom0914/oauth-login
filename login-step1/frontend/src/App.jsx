import "bootstrap/dist/css/bootstrap.min.css"
import { Route, Routes } from "react-router-dom"
import HomePage from "./components/pages/HomePage"
import LoginView from "./components/auth/LoginView"
import GoogleRedirect from "./components/auth/GoogleRedirect"
import KakaoRedirect from "./components/auth/KakaoRedirect"

function App() {

  return (
    <>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginView />} />
        <Route path="/oauth/google/redirect" element={<GoogleRedirect />} />
        <Route path="/oauth/kakao/redirect" element={<KakaoRedirect />} />
      </Routes>
    </>
  )
}

export default App
