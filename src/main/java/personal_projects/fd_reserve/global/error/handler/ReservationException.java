package personal_projects.fd_reserve.global.error.handler;

import personal_projects.fd_reserve.global.error.code.BaseErrorCode;
import personal_projects.fd_reserve.global.error.exception.GeneralException;

public class ReservationException extends GeneralException {
    public ReservationException(BaseErrorCode code) {
        super(code);
    }
}
