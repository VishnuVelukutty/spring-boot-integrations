import { useNavigate } from "react-router-dom";

function Logout() {

    const navigate = useNavigate();
    
    const handleLogout = (event) => {
        event.preventDefault();
        UserService.logout();
        navigate("/");

    }

    return (
        <>
            <button type="submit" onClick={handleLogout}>Register</button>
        </>
    )
}


export default Logout