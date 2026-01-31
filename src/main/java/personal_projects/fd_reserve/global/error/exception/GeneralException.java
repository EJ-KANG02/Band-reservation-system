package personal_projects.fd_reserve.global.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import personal_projects.fd_reserve.global.error.code.BaseErrorCode;
import personal_projects.fd_reserve.global.error.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

  private BaseErrorCode code;

  public ErrorReasonDTO getErrorReason() {
    return this.code.getReason();
  }

  public ErrorReasonDTO getErrorReasonHttpStatus(){
    return this.code.getReasonHttpStatus();
  }
}