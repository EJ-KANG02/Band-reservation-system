package personal_projects.fd_reserve.domain.Setting.dto;

import lombok.*;

import java.time.LocalDateTime;

public class SettingDTO {

    public static class SettingRequest{

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UpdateRequest {
            private Integer ensembleMaxCountPerWeek;
            private Integer ensembleMaxTime;
            private Integer drumMaxCountPerWeek;
            private Integer drumMaxTime;
        }
    }

    public static class SettingResponse{

        @Builder
        @Getter
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        @AllArgsConstructor(access = AccessLevel.PROTECTED)
        public static class UpdateResponse{
            private Integer ensembleMaxCountPerWeek;
            private Integer ensembleMaxTime;
            private Integer drumMaxCountPerWeek;
            private Integer drumMaxTime;
            private LocalDateTime updatedAt;
        }
    }
}
