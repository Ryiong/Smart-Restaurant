package com.example.SmartRestaurant.common;

public final class Constant {
    public static final String URL = "smart-restaurant/v1";
    public static final String sendOTP = "<!DOCTYPE html>\n" +
            "<html lang=\"vi\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\" />\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
            "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
            "<title>Mã xác thực OTP</title>\n" +
            "<!--[if mso]>\n" +
            "<noscript>\n" +
            "<xml>\n" +
            "<o:OfficeDocumentSettings>\n" +
            "<o:PixelsPerInch>96</o:PixelsPerInch>\n" +
            "</o:OfficeDocumentSettings>\n" +
            "</xml>\n" +
            "</noscript>\n" +
            "<![endif]-->\n" +
            "<style>\n" +
            "  body, table, td, a { -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; }\n" +
            "  table, td { mso-table-lspace: 0pt; mso-table-rspace: 0pt; }\n" +
            "  img { -ms-interpolation-mode: bicubic; border: 0; height: auto; line-height: 100%; outline: none; text-decoration: none; }\n" +
            "  body { margin: 0; padding: 0; width: 100% !important; height: 100% !important; background-color: #f4f4f7; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; }\n" +
            "\n" +
            "  .otp-code {\n" +
            "    font-family: 'Courier New', Courier, monospace;\n" +
            "    font-size: 36px;\n" +
            "    font-weight: 700;\n" +
            "    letter-spacing: 10px;\n" +
            "    color: #d9480f;\n" +
            "    background-color: #fff4e6;\n" +
            "    border: 1px dashed #f0a35a;\n" +
            "    border-radius: 8px;\n" +
            "    padding: 16px 24px;\n" +
            "    display: inline-block;\n" +
            "  }\n" +
            "\n" +
            "  @media only screen and (max-width: 600px) {\n" +
            "    .email-container { width: 100% !important; }\n" +
            "    .otp-code { font-size: 28px !important; letter-spacing: 6px !important; padding: 12px 16px !important; }\n" +
            "    .inner-padding { padding: 24px !important; }\n" +
            "  }\n" +
            "</style>\n" +
            "</head>\n" +
            "<body style=\"margin:0; padding:0; background-color:#f4f4f7;\">\n" +
            "\n" +
            "  <!-- Preheader (ẩn, hiện trên phần preview của inbox) -->\n" +
            "  <div style=\"display:none; max-height:0; overflow:hidden; mso-hide:all;\">\n" +
            "    Mã OTP của bạn sẽ hết hạn sau 5 phút. Vui lòng không chia sẻ mã này với bất kỳ ai.\n" +
            "  </div>\n" +
            "\n" +
            "  <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"background-color:#f4f4f7;\">\n" +
            "    <tr>\n" +
            "      <td align=\"center\" style=\"padding: 40px 16px;\">\n" +
            "\n" +
            "        <table role=\"presentation\" class=\"email-container\" width=\"600\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px; max-width:600px; background-color:#ffffff; border-radius:12px; overflow:hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.06);\">\n" +
            "\n" +
            "          <!-- Header / Logo -->\n" +
            "          <tr>\n" +
            "            <td align=\"center\" style=\"background-color:#1f2937; padding: 28px 24px;\">\n" +
            "              <!-- Thay bằng logo thật, hoặc để text -->\n" +
            "              <span style=\"color:#ffffff; font-size:20px; font-weight:700; font-family: Arial, sans-serif;\">\n" +
            "                Smart Restaurant\n" +
            "              </span>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "\n" +
            "          <!-- Nội dung chính -->\n" +
            "          <tr>\n" +
            "            <td class=\"inner-padding\" style=\"padding: 40px;\">\n" +
            "              <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
            "                <tr>\n" +
            "                  <td align=\"center\">\n" +
            "                    <h1 style=\"margin:0 0 8px; font-size:22px; color:#111827; font-family: Arial, sans-serif;\">\n" +
            "                      Xác thực tài khoản của bạn\n" +
            "                    </h1>\n" +
            "                    <p style=\"margin:0 0 28px; font-size:15px; line-height:1.6; color:#6b7280; font-family: Arial, sans-serif;\">\n" +
            "                      Xin chào <strong>{{HO_TEN}}</strong>,<br />\n" +
            "                      Vui lòng sử dụng mã xác thực (OTP) bên dưới để hoàn tất yêu cầu của bạn.\n" +
            "                    </p>\n" +
            "                  </td>\n" +
            "                </tr>\n" +
            "\n" +
            "                <!-- Mã OTP -->\n" +
            "                <tr>\n" +
            "                  <td align=\"center\" style=\"padding: 8px 0 28px;\">\n" +
            "                    <span class=\"otp-code\">{{MA_OTP}}</span>\n" +
            "                  </td>\n" +
            "                </tr>\n" +
            "\n" +
            "                <tr>\n" +
            "                  <td align=\"center\">\n" +
            "                    <p style=\"margin:0 0 4px; font-size:14px; color:#6b7280; font-family: Arial, sans-serif;\">\n" +
            "                      Mã sẽ hết hạn sau <strong style=\"color:#111827;\">5 phút</strong>.\n" +
            "                    </p>\n" +
            "                    <p style=\"margin:0; font-size:14px; color:#6b7280; font-family: Arial, sans-serif;\">\n" +
            "                      Vui lòng không chia sẻ mã này với bất kỳ ai, kể cả nhân viên của chúng tôi.\n" +
            "                    </p>\n" +
            "                  </td>\n" +
            "                </tr>\n" +
            "\n" +
            "                <!-- Đường kẻ phân cách -->\n" +
            "                <tr>\n" +
            "                  <td style=\"padding: 28px 0;\">\n" +
            "                    <hr style=\"border:none; border-top:1px solid #e5e7eb; margin:0;\" />\n" +
            "                  </td>\n" +
            "                </tr>\n" +
            "\n" +
            "                <tr>\n" +
            "                  <td align=\"center\">\n" +
            "                    <p style=\"margin:0; font-size:13px; line-height:1.6; color:#9ca3af; font-family: Arial, sans-serif;\">\n" +
            "                      Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email này hoặc liên hệ với chúng tôi ngay để được hỗ trợ.\n" +
            "                    </p>\n" +
            "                  </td>\n" +
            "                </tr>\n" +
            "              </table>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "\n" +
            "          <!-- Footer -->\n" +
            "          <tr>\n" +
            "            <td align=\"center\" style=\"background-color:#f9fafb; padding: 24px 32px;\">\n" +
            "              <p style=\"margin:0 0 4px; font-size:12px; color:#9ca3af; font-family: Arial, sans-serif;\">\n" +
            "                © 2026 Smart Restaurant. Mọi quyền được bảo lưu.\n" +
            "              </p>\n" +
            "              <p style=\"margin:0; font-size:12px; color:#9ca3af; font-family: Arial, sans-serif;\">\n" +
            "                Đây là email tự động, vui lòng không trả lời email này.\n" +
            "              </p>\n" +
            "            </td>\n" +
            "          </tr>\n" +
            "\n" +
            "        </table>\n" +
            "\n" +
            "      </td>\n" +
            "    </tr>\n" +
            "  </table>\n" +
            "\n" +
            "</body>\n" +
            "</html>";


}
