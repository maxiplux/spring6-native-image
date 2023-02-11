package app.quantun.api.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationTokenDto {
  private String access_token;
   private String token_type;
   private String refresh_token;


}
