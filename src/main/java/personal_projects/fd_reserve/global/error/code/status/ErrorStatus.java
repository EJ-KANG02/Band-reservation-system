package personal_projects.fd_reserve.global.error.code.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import personal_projects.fd_reserve.global.error.code.BaseErrorCode;
import personal_projects.fd_reserve.global.error.code.ErrorReasonDTO;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),

    //USER 관련 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER4001", "유저 정보가 존재하지 않습니다."),
    USER_ALREADY_EXISTIS(HttpStatus.MULTI_STATUS, "USER4002", "이미 계정이 존재합니다."),
    NICKNAME_ALREADY_EXISTS(HttpStatus.MULTI_STATUS, "USER4003", "중복된 닉네임입니다."),
    OFFICER_NOT_APPROVED(HttpStatus.FORBIDDEN, "USER4004", "회장단 승인이 되지 않은 계정입니다."),

    //RESERVATION 관련 에러
    TEAM_NAME_NOT_FOUND(HttpStatus.NOT_FOUND, "RESERVATION4001", "속한 팀이 없습니다."),
    RESERVATION_TIME_CONFLICT(HttpStatus.CONFLICT, "RESERVATION4002", "기존 시간표와 겹칩니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
