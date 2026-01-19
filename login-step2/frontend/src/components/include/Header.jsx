import { useEffect, useState } from "react"
import { Button, Container, Nav, Navbar } from "react-bootstrap"
import { Link, useNavigate } from "react-router-dom"
// Header함수가 호출되었을 때 먼저 access token을 체크(확인)하자
// access token은 어디에 저장되어 있나요? - 
const Header = () => {
  const navigate = useNavigate()
  //로그인 상태 관리 - false이면 로그아웃버튼이 보이지 않음
  const [email, setEmail] = useState('')
  const [username, setUserName] = useState('')
  //리액트 - 동기화 - 새로 그려진다. - 언제?
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  //insert here
  useEffect (() => {
    const token = window.localStorage.getItem("accessToken")
    const email = window.localStorage.getItem("email")
    const name = window.localStorage.getItem("username")
    if(token){//access token이 존재하면 로그인을 하였다.
      setIsLoggedIn(true)
      setEmail(email)
      setUserName(name)
    }
  },[])
  const onLogout = () => {
    setIsLoggedIn(false)
    window.localStorage.clear()
    navigate("/")
  }
  return (
    <>
      <Navbar expand="lg" className="bg-body-tertiary">
        <Container>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="me-auto">
              <Link to="/" className="nav-link">Home</Link>
              {!isLoggedIn &&
              <Link to="/login" className="nav-link">로그인</Link>
              }
              <Link to="/board" className="nav-link">게시판</Link>
            </Nav>
            {isLoggedIn &&
            <>
              <Link to="/mypage" className="nav-link">{email}</Link>
              <Button className="btn btn-danger" onClick={onLogout}>로그아웃</Button>
            </>
            }
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  )
}

export default Header