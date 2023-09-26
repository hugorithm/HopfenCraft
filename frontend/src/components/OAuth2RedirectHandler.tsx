import React, { Component } from 'react';
import { ACCESS_TOKEN } from '../config/constants';
import { Navigate, useLocation  } from 'react-router-dom';


class OAuth2RedirectHandler extends Component{
  location = useLocation();
  
  getUrlParameter(name: string) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');

    const results = regex.exec(location.search);

    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

  render() {
    const token = this.getUrlParameter('token');
    const error = this.getUrlParameter('error');

    if (token) {
      

      return (
        <Navigate to="/profile" />
      );
    } else {
      return (
        <Navigate to="/login" />
      );
    }
  }
}

export default OAuth2RedirectHandler;
