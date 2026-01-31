package personal_projects.fd_reserve.global.error.handler;

import personal_projects.fd_reserve.global.error.code.BaseErrorCode;
import personal_projects.fd_reserve.global.error.code.status.ErrorStatus;
import personal_projects.fd_reserve.global.error.exception.GeneralException;

public class UserException extends GeneralException {
    public UserException(BaseErrorCode code) {
        super(code);
    }
}
