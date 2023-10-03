import { isRejectedWithValue } from '@reduxjs/toolkit';
import type { MiddlewareAPI, Middleware } from '@reduxjs/toolkit';
import { toast } from 'react-toastify';
import SessionExpired from '../../components/SessionExpired';



export const rtkQueryErrorLogger: Middleware =
  (api: MiddlewareAPI) => (next) => (action) => {
 
    if (isRejectedWithValue(action)) {
      console.log(action)
      if (action.payload.status === 401 && !action.type.includes('authApi')) {
        localStorage.removeItem("user");

        toast.info(<SessionExpired/>, {
          position: "top-center",
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: false,
          draggable: true,
          pauseOnFocusLoss: false,
          autoClose: false,
          toastId: "expired"
        });
      } 
    }
  
    return next(action)
  }