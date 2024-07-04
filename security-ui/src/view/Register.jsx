import { useNavigate } from "react-router-dom";


export default function Register() {

    const navigate = useNavigate();


    const handleLogin = () => {
        navigate('/admin/home')
    }

    const handleRegister = () => {

    }



    return (
        <>
            <div>
                First Name : <input type="text" id="fname"></input>
            </div> 
            <div>
                Last Name : <input type="text" id="lname"></input>
            </div> 
            <div>
                Username : <input type="text" id="uname"></input>
            </div>    
            <div>
                Password : <input type="password" id="pass"></input>
            </div>
            <div>
                <button type="submit" onClick={handleLogin}>Main Page</button>
            </div>
            <div>
                <button type="submit" onClick={handleRegister}>Register</button>
            </div>
        </>
    )
}