import { jwtDecode } from 'jwt-decode'
import React, { useEffect, useState } from 'react'

const IsTokenExpiration = (token) => {
  console.log(token)
  const [isTokenExpired, setIsTokenExpired] = useState(false)
  const [isUserResponsed, setIsUserResponsed] = useState(false)
  const [isCheck, setIsCheck] = useState(true)
  
  useEffect(() => {
    const checkTokenExpiration = () => {
      //1. 토큰이 아예 없을 때 진행되지 않음
      if(!token){
        setIsTokenExpired(true)
        return 
      }

      //2. JWT 디코딩
      const decoded = jwtDecode(token)
      console.log(decoded.exp) //확인함

      //3. 현재 시간을 초 단위로 만든다.
      const currentTime = Date.now() / 1000

      //4. 만료시간 < 현재시간 이면 이미 만료된 토큰임
      if(decoded.exp < currentTime){
        setIsTokenExpired(true)
      }//end of if
      //5. 토큰이 아직 유효한 경우
      else{
        const remainTime = decoded.exp - currentTime
        console.log(remainTime)

        //6. 만료가 60초 보다 작거나 같으면 토큰을 연장하겠습니까? 라고 묻는다.

      }//end of else 


    }
    checkTokenExpiration()
  },[])

  return isTokenExpired
}

export default IsTokenExpiration

/* - accessToken(JWT)이 만료되었는지 검사한다.
 * - 만료가 임박(1분 이내)하면 confirm 창을 띄워서 연장할지 물어본다.
 * - 사용자가 연장에 동의하면 refresh token으로 access token을 재발급 받는다.
 * - 최종적으로 "만료 여부(boolean)"를 반환한다.
 * 
 * 1. 토큰이 만료됨 or 아직 유효함
 * isTokenExpired : false
 * 
 * 2. 로그인 연장화면에서 확인/취소를 누를 때
 * isUserResponsed : false
 * 확인(true) :  연장하기 동의한 경우
 * 취소(false): 연장 성공 시 다시 false로 돌리기
 * 다음 만료 임박 때 다시 물어봐야 함
 * 
 * 3. 토큰 연장에 대한 팝업 호출 제어
 * isCheck - 디폴트: true
 * true를 false로 변경해서 추가 알림창이 뜨는것을 방지함
 * 
  * 실제 만료 체크/갱신 로직이 들어있는 함수
  * - token이 없다면 => 만료 처리
  * - token이 있다면 jwtDecode로 exp(만료시간)를 확인
  * - 만료되었으면 => 만료 처리
  * - 아직 유효하면 => 남은 시간(remainTime)을 계산
  * - 남은 시간이 60초 이하이고, 아직 confirm 응답을 안 했으면 confirm 창 띄움
  
*/