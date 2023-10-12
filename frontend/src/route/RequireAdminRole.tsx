import { Outlet } from 'react-router-dom';
import NotFound from '../errors/NotFound';

const RequireAdminRole = () => {
    const roles: string[] = JSON.parse(localStorage.getItem("user") || "{}").roles;

    return (
        roles.includes("ADMIN") ? <Outlet /> : <NotFound />
    )

};

export default RequireAdminRole;