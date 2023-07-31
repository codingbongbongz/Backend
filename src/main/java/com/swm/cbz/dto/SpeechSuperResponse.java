package com.swm.cbz.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeechSuperResponse {

    @JsonProperty("result")
    private Map<String, Object> result;

    public Long getOverall() {
        return ((Number) result.get("overall")).longValue();
    }

    public Long getPronunciation() {
        return ((Number) result.get("pronunciation")).longValue();
    }

    public Long getFluency() {
        return ((Number) result.get("fluency")).longValue();
    }

    public Long getIntegrity() {
        return ((Number) result.get("integrity")).longValue();
    }

    public Long getRhythm() {
        return ((Number) result.get("rhythm")).longValue();
    }

    public Long getSpeed() {
        return ((Number) result.get("speed")).longValue();
    }

    public String getDuration() {
        return (String) result.get("duration");
    }

    public String getKernelVersion() {
        return (String) result.get("kernel_version");
    }
}
