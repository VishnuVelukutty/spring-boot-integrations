import { useNavigate } from "react-router-dom";
import MainService from "../service/MainService";

function Logout() {

    const navigate = useNavigate();

    const handleLogout = (event) => {
        event.preventDefault();
        MainService.logout();
        navigate("/");
    }


    function handlePage(event) {
        event.preventDefault();

        if (MainService.isAuthenticated() && MainService.isUser()) {
            navigate('/user/home');
        }
        else if (MainService.adminOnly()) {
            navigate('/admin/home');
        }
    }

    return (
        <>

            <button type="submit" onClick={handlePage}>Main Page</button>

            <button type="submit" onClick={handleLogout}>Logout</button>
        </>
    )
}


export default Logout