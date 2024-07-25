import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from "react-router-dom";

import Login from "./view/Login";
import DisplayOnLogin from './view/DisplayOnLogin';
import DisplayOnUser from './view/DisplayOnUser';
import DisplayOnAdmin from './view/DisplayOnAdmin';
import Register from './view/Register';
import MainService from './service/MainService';

function App() {

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userType, setUserType] = useState("");

  const recieveType = (data) => {
    setUserType(data);
    console.log("Usertype rcvd in app.js >>> " + userType);
  };

  const handleLogin = () => {
    setIsLoggedIn(true);
    setUserName(userName); // Call the function with the received username
  };


  function handleLogOut(event) {
    event.preventDefault();
    setIsLoggedIn(false);
  }



  return (
    <>
      <Router>
        <Routes>
          <Route path='/' element={<Login />} />
          <Route path='/register' element={<Register />} />


          {(MainService.isAuthenticated() && MainService.isUser()) && (
            <>
              <Route path='/content' element={<DisplayOnLogin />} />
              <Route path='/user/home' element={<DisplayOnUser />} />
            </>
          )}

          {(MainService.adminOnly()) && (

            <>              
            <Route path='/content' element={<DisplayOnLogin />} />
            <Route path='/admin/home' element={<DisplayOnAdmin />} />
            </>

          )}

        </Routes>
      </Router>

    </>
  )
}


/*
 {isLoggedIn ? (<Router>
      <Routes>
        <Route path='/content' element={<DisplayOnLogin/>}/>
        <Route path='/admin/home' element={<DisplayOnAdmin/>}/>
        <Route path='/user/home' element={<DisplayOnUser/>}/>
        <Route path='/display' element={<DisplayDetails/>}/>
      </Routes>
    </Router>) : (    <Login loginStatus={handleLogin} role={recieveType}/>
)}
 */

/* 
<Router>
        <Routes>
          <Route path='/' element={<Login />} />
          <Route path='/content' element={<DisplayOnLogin/>}/>
          <Route path='/admin/home' element={<DisplayOnAdmin/>}/>
          <Route path='/user/home' element={<DisplayOnUser/>}/>
        </Routes>
</Router>
*/
export default App
