import "bootstrap/dist/css/bootstrap.min.css"
import { Route, Routes } from "react-router-dom"
import HomePage from "./components/pages/HomePage"
import BoardPage from "./components/pages/BoardPage"

function App() {
  const token = window.localStorage.getItem('token')
  //TODO - 토큰 유효시간 체크 - 파기/유지
  
  return (
    <>
      <Routes>
        <Route path="/home" element={<HomePage />} />
        <Route path="/board" element={<BoardPage />} />
      </Routes>
    </>
  )
}

export default App
