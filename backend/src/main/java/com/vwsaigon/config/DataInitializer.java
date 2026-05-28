package com.vwsaigon.config;

import com.vwsaigon.entity.Admin;
import com.vwsaigon.entity.CarModel;
import com.vwsaigon.entity.NewsPost;
import com.vwsaigon.repository.AdminRepository;
import com.vwsaigon.repository.CarModelRepository;
import com.vwsaigon.repository.NewsPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final CarModelRepository carModelRepository;
    private final NewsPostRepository newsPostRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username:admin}")
    private String defaultUsername;

    @Value("${app.admin.password:Admin@2024!}")
    private String defaultPassword;

    @Override
    public void run(String... args) {
        if (adminRepository.count() == 0) {
            Admin admin = Admin.builder()
                    .username(defaultUsername)
                    .passwordHash(passwordEncoder.encode(defaultPassword))
                    .build();
            adminRepository.save(admin);
            log.info("Created default admin account: {}", defaultUsername);
        }

        if (carModelRepository.count() == 0) {
            List<CarModel> models = List.of(
                CarModel.builder().name("Tiguan Facelift").slug("tiguan-facelift").category("SUV")
                    .price(1699000000L).priceDisplay("1.699.000.000")
                    .shortDescription("SUV đô thị thông minh, thiết kế facelift mới nhất với công nghệ IQ.DRIVE.")
                    .description("Volkswagen Tiguan Facelift là chiếc SUV đô thị được thiết kế lại theo ngôn ngữ IQ. mới nhất. Đèn LED ma trận IQ.Light, lưới tản nhiệt phẳng và cản trước/sau hoàn toàn mới tạo nên diện mạo sắc sảo, hiện đại.\n\nNội thất trang bị Digital Cockpit Pro 10 inch, màn hình cảm ứng 9.2 inch, hệ thống IQ.DRIVE đầy đủ gồm Lane Assist, Front Assist và Adaptive Cruise Control.")
                    .engine("1.5L TSI EVO").power("150 mã lực").torque("250 Nm").seats(5).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("GVbe1KgEmAU").featured(true).active(true).build(),

                CarModel.builder().name("Teramont USA Base").slug("teramont-usa-base").category("SUV")
                    .price(1999000000L).priceDisplay("1.999.000.000")
                    .shortDescription("SUV 7 chỗ nhập Mỹ phiên bản Base, chất lượng tiêu chuẩn Bắc Mỹ.")
                    .description("Teramont USA Base nhập khẩu nguyên chiếc từ Mỹ, đáp ứng tiêu chuẩn sản xuất Bắc Mỹ nghiêm ngặt. Không gian nội thất rộng rãi với 3 hàng ghế thoải mái, đặc biệt hàng ghế 3 đủ rộng cho người lớn.\n\nĐộng cơ 2.0L TSI mạnh mẽ kết hợp hộp số DSG 7 cấp, hệ thống an toàn IQ.DRIVE chuẩn Mỹ.")
                    .engine("2.0L TSI").power("220 mã lực").torque("350 Nm").seats(7).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("93RZ0hXDbvQ").featured(true).active(true).build(),

                CarModel.builder().name("Teramont USA Limited").slug("teramont-usa-limited").category("SUV")
                    .price(2299000000L).priceDisplay("2.299.000.000")
                    .shortDescription("SUV 7 chỗ nhập Mỹ phiên bản Limited với trang bị cao cấp vượt trội.")
                    .description("Teramont USA Limited là phiên bản cao cấp hơn của dòng Teramont nhập Mỹ. Nội thất ghế da cao cấp, cửa sổ trời panorama toàn cảnh, mâm 20 inch và hệ thống âm thanh 8 loa premium.\n\nDẫn động 4 bánh 4Motion AWD toàn thời gian, camera 360°, Head-up Display và sạc không dây chuẩn Qi.")
                    .engine("2.0L TSI").power("220 mã lực").torque("350 Nm").seats(7).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("LbLNp-fTnJg").featured(true).active(true).build(),

                CarModel.builder().name("Teramont President").slug("teramont-president").category("SUV")
                    .price(2499000000L).priceDisplay("2.499.000.000")
                    .shortDescription("SUV 7 chỗ phiên bản President — đỉnh cao sang trọng của dòng Teramont.")
                    .description("Teramont President là phiên bản tối thượng, được trang bị những tiện nghi sang trọng nhất. Da Nappa cao cấp, ốp gỗ thực, đèn viền 30 màu và ghế massage hàng trước.\n\nMàn hình trung tâm 12 inch, Digital Cockpit Pro 10.25 inch, Head-up Display và camera 360° là tiêu chuẩn. Cửa sổ trời toàn cảnh 2 tầng mang ánh sáng tràn ngập không gian.")
                    .engine("2.0L TSI").power("220 mã lực").torque("350 Nm").seats(7).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("3f3MIgXjqLc").featured(true).active(true).build(),

                CarModel.builder().name("Teramont X Platinum").slug("teramont-x-platinum").category("SUV")
                    .price(2349000000L).priceDisplay("2.349.000.000")
                    .shortDescription("SUV 7 chỗ Teramont X phiên bản Platinum sang trọng tột cùng.")
                    .description("Teramont X Platinum là phiên bản cao cấp nhất của Teramont X, tích hợp hệ thống âm thanh Dynaudio 12 loa 480W. Nội thất da Nappa, ốp gỗ thực và chrome toàn bộ, đẳng cấp không kém xe hạng sang.\n\nMàn hình trung tâm 12 inch kết hợp Digital Cockpit Pro 10.25 inch. Panoramic sunroof và đèn viền 30 màu.")
                    .engine("2.0L TSI").power("220 mã lực").torque("350 Nm").seats(7).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("-BukoMPoDCs").featured(true).active(true).build(),

                CarModel.builder().name("Viloran Premium").slug("viloran-premium").category("MPV")
                    .price(1799000000L).priceDisplay("1.799.000.000")
                    .shortDescription("MPV sang trọng phiên bản Premium cho gia đình hiện đại.")
                    .description("Viloran Premium mang đến không gian MPV rộng rãi với 3 hàng ghế thoải mái. Điều hòa 3 vùng độc lập, 6 cổng USB toàn xe và màn hình giải trí 9.2 inch.\n\nHệ thống an toàn IQ.DRIVE đầy đủ, thiết kế ngoại thất sang trọng với đèn LED và mâm 18 inch thể thao.")
                    .engine("2.0L TSI").power("220 mã lực").torque("350 Nm").seats(7).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("D9L3oHC4aa0").featured(true).active(true).build(),

                CarModel.builder().name("Viloran Luxury").slug("viloran-luxury").category("MPV")
                    .price(2099000000L).priceDisplay("2.099.000.000")
                    .shortDescription("MPV executive phiên bản Luxury — trải nghiệm di chuyển đẳng cấp hạng nhất.")
                    .description("Viloran Luxury là phiên bản cao cấp nhất, biến mỗi chuyến đi thành trải nghiệm hạng nhất. Ghế hàng 2 kiểu thương gia có mát-xa 10 điểm, chỉnh điện và để chân đầy đủ.\n\nĐèn viền nội thất 30 màu, kính cách âm 2 lớp và hệ thống thông gió chủ động. Mái kính panorama toàn cảnh và âm thanh cao cấp.")
                    .engine("2.0L TSI").power("220 mã lực").torque("350 Nm").seats(7).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("PKg4sT5ErdA").featured(true).active(true).build(),

                CarModel.builder().name("Golf 1.5 eTSI").slug("golf-15-etsi").category("Hatchback")
                    .price(797000000L).priceDisplay("797.000.000")
                    .shortDescription("Hatchback thể thao tiết kiệm nhiên liệu với công nghệ eTSI Mild Hybrid.")
                    .description("Golf 1.5 eTSI trang bị công nghệ mild hybrid 48V, giảm tiêu hao nhiên liệu đến 15% so với phiên bản thường. Động cơ 1.5L TSI EVO kết hợp hộp số DSG 7 cấp mang lại trải nghiệm lái thú vị và tiết kiệm.\n\nThiết kế hatchback thể thao trẻ trung, Digital Cockpit 10 inch và màn hình cảm ứng 8.25 inch. Đèn LED matrix và mâm 16 inch thể thao.")
                    .engine("1.5L eTSI").power("130 mã lực").torque("200 Nm").seats(5).fuelType("Xăng Hybrid").transmission("DSG 7 cấp")
                    .videoUrl("oQp9KxVqiuQ").featured(true).active(true).build(),

                CarModel.builder().name("Golf 2.0").slug("golf-20").category("Hatchback")
                    .price(1898000000L).priceDisplay("1.898.000.000")
                    .shortDescription("Hatchback hiệu suất cao với động cơ 2.0 TSI mạnh mẽ và body kit R-Line thể thao.")
                    .description("Golf 2.0 TSI là phiên bản hiệu suất cao nhất của dòng Golf tại Việt Nam. Động cơ 2.0L TSI mạnh mẽ kết hợp hộp số DSG 7 cấp cho khả năng tăng tốc ấn tượng.\n\nBody kit R-Line thể thao, mâm 18 inch đen bóng, nội thất sport với ghế racing và vô lăng thể thao. Hệ thống lái thể thao Progressive Steering.")
                    .engine("2.0L TSI").power("190 mã lực").torque("320 Nm").seats(5).fuelType("Xăng").transmission("DSG 7 cấp")
                    .videoUrl("y9MeLRO1sIs").featured(true).active(true).build(),

                CarModel.builder().name("Touareg Elegance").slug("touareg-elegance").category("SUV")
                    .price(2899000000L).priceDisplay("2.899.000.000")
                    .shortDescription("SUV flagship phiên bản Elegance tinh tế, kết hợp sang trọng và công nghệ.")
                    .description("Touareg Elegance mang ngôn ngữ thiết kế thanh lịch với Innovision Cockpit màn hình cong 15 inch. Ngoại thất mâm 19 inch chrome, lưới tản nhiệt mạ bạc và đèn LED matrix tinh tế.\n\nHệ thống treo khí 4 cấp, động cơ V6 TDI 3.0L mạnh mẽ và dẫn động 4Motion. Air suspension cho phép thay đổi độ cao gầm theo địa hình.")
                    .engine("3.0L V6 TDI").power("231 mã lực").torque("500 Nm").seats(5).fuelType("Xăng").transmission("Tiptronic 8 cấp")
                    .videoUrl("do_O-fuB-RE").featured(true).active(true).build(),

                CarModel.builder().name("Touareg R-Line").slug("touareg-rline").category("SUV")
                    .price(2999000000L).priceDisplay("2.999.000.000")
                    .shortDescription("SUV flagship phiên bản R-Line thể thao mạnh mẽ, cá tính vượt trội.")
                    .description("Touareg R-Line mang phong cách thể thao với body kit R-Line đặc trưng, mâm 20 inch đen bóng và hệ thống xả thể thao đôi. Nội thất sport với đường chỉ khâu màu đỏ và vô lăng thể thao.\n\n5 chế độ lái từ Eco đến Off-road, hệ thống treo khí và dẫn động 4Motion AWD toàn thời gian.")
                    .engine("3.0L V6 TDI").power("231 mã lực").torque("500 Nm").seats(5).fuelType("Xăng").transmission("Tiptronic 8 cấp")
                    .videoUrl("uJoRgj_b8ZY").featured(false).active(true).build(),

                CarModel.builder().name("Touareg Highline").slug("touareg-highline").category("SUV")
                    .price(3499000000L).priceDisplay("3.499.000.000")
                    .shortDescription("SUV flagship phiên bản Highline — tích hợp mọi công nghệ đỉnh cao nhất.")
                    .description("Touareg Highline là phiên bản tối thượng, tích hợp toàn bộ công nghệ cao cấp nhất của Volkswagen. Head-up Display thực tế tăng cường AR, ghế massage 10 điểm và thông gió chủ động.\n\nNight Vision, Bose Surround 14 loa, da Nappa toàn bộ và ốp gỗ thực. Mâm 21 inch và cản thể thao tạo nên tổng thể đẳng cấp tối thượng.")
                    .engine("3.0L V6 TDI").power("231 mã lực").torque("500 Nm").seats(5).fuelType("Xăng").transmission("Tiptronic 8 cấp")
                    .videoUrl("83qH1PkYUVo").featured(false).active(true).build()
            );
            carModelRepository.saveAll(models);
            log.info("Seeded {} car models", models.size());
        }

        if (newsPostRepository.count() == 0) {
            List<NewsPost> posts = List.of(
                NewsPost.builder()
                    .slug("tiguan-facelift-2025")
                    .category("Xe mới")
                    .title("Volkswagen Tiguan Facelift 2025 chính thức ra mắt tại Việt Nam")
                    .excerpt("Phiên bản nâng cấp mang đến thiết kế hiện đại hơn cùng loạt công nghệ an toàn tiên tiến.")
                    .imageUrl("/images/news/news1.jpg")
                    .published(true)
                    .content("<p>Volkswagen An Phú chính thức giới thiệu Tiguan Facelift 2025 tại thị trường Việt Nam, đánh dấu bước nâng cấp toàn diện cho mẫu SUV đô thị được yêu thích nhất của thương hiệu.</p>\n\n<h2>Thiết kế ngoại thất mới</h2>\n<p>Tiguan 2025 khoác lên mình ngôn ngữ thiết kế IQ. mới nhất của Volkswagen với lưới tản nhiệt được làm phẳng hoàn toàn, kết hợp cùng cụm đèn pha LED ma trận IQ.Light tinh tế. Dải đèn LED chạy dọc theo chiều rộng xe tạo nên nhận diện đặc trưng ngay cả trong bóng tối.</p>\n<p>Bộ mâm xe hợp kim 18 inch kiểu mới, cản trước và sau được thiết kế lại mạnh mẽ hơn, mang đến tổng thể ngoại hình vừa thanh lịch vừa năng động.</p>\n\n<h2>Công nghệ nội thất vượt trội</h2>\n<p>Khoang lái hoàn toàn mới với hệ thống Digital Cockpit Pro tích hợp màn hình 10 inch kỹ thuật số thay thế hoàn toàn các đồng hồ cơ truyền thống. Màn hình cảm ứng trung tâm 9.2 inch chạy hệ thống thông tin giải trí MIB3 hỗ trợ kết nối không dây với Apple CarPlay và Android Auto.</p>\n<p>Vô lăng đa chức năng mới với thiết kế phẳng đáy, tích hợp các phím bấm cảm ứng và nút bấm vật lý được bố trí khoa học. Ghế ngồi có thể điều chỉnh điện với chức năng sưởi hàng trước theo tiêu chuẩn.</p>\n\n<h2>Hệ thống an toàn IQ.DRIVE</h2>\n<p>Tiguan 2025 được trang bị đầy đủ bộ công nghệ hỗ trợ lái thông minh IQ.DRIVE bao gồm: hỗ trợ giữ làn đường Lane Assist, phanh khẩn cấp tự động Front Assist với nhận diện người đi bộ, kiểm soát hành trình thích ứng ACC, hỗ trợ đỗ xe Park Assist và camera 360 độ.</p>\n\n<h2>Giá và phiên bản</h2>\n<p>Volkswagen Tiguan Facelift 2025 được phân phối tại Việt Nam với 2 phiên bản: Elegance và R-Line, giá bán từ 1.699.000.000 VNĐ. Xe hiện có sẵn tại showroom Volkswagen An Phú để khách hàng trải nghiệm thực tế.</p>\n<p>Liên hệ hotline <strong>098 105 8232</strong> để đặt lịch lái thử miễn phí hoặc nhận tư vấn chi tiết từ đội ngũ chuyên viên của chúng tôi.</p>")
                    .build(),

                NewsPost.builder()
                    .slug("teramont-x-platinum")
                    .category("Sự kiện")
                    .title("Ra mắt Teramont X Platinum – Đỉnh cao của dòng SUV 7 chỗ")
                    .excerpt("Teramont X Platinum nâng cấp nội thất, trang bị thêm màn hình panorama và hệ thống âm thanh Dynaudio cao cấp.")
                    .imageUrl("/images/news/news2.jpg")
                    .published(true)
                    .content("<p>Volkswagen An Phú vừa tổ chức lễ ra mắt chính thức Teramont X Platinum 2025 — phiên bản đỉnh cao nhất trong dòng SUV 7 chỗ của Volkswagen tại Việt Nam, thu hút sự quan tâm đặc biệt từ giới yêu xe.</p>\n\n<h2>Nội thất Platinum đẳng cấp</h2>\n<p>Điểm nhấn nổi bật nhất của Teramont X Platinum là khoang nội thất được nâng cấp toàn diện với chất liệu da Nappa cao cấp bọc toàn bộ ghế ngồi, taplo và cửa xe. Ốp gỗ thực màu Nut Brown được sắp xếp tinh tế xuyên suốt cabin, kết hợp cùng các chi tiết chrome tạo nên không gian sang trọng không kém xe hạng sang.</p>\n\n<h2>Hệ thống âm thanh Dynaudio</h2>\n<p>Lần đầu tiên xuất hiện trên Teramont X tại Việt Nam, hệ thống âm thanh Dynaudio Confidence 12 loa với công suất 480W mang đến trải nghiệm âm nhạc đỉnh cao ngay trong lòng xe. Dynaudio — thương hiệu loa Hi-Fi hàng đầu Đan Mạch — đã tinh chỉnh đặc biệt hệ thống này để phù hợp với khoang âm thanh của Teramont X.</p>\n\n<h2>Màn hình Panorama và công nghệ</h2>\n<p>Màn hình trung tâm 12 inch với độ phân giải cao kết hợp cùng Digital Cockpit Pro 10.25 inch tạo nên buồng lái kỹ thuật số toàn diện. Mái kính Panorama toàn cảnh 2 tầng mang ánh sáng tự nhiên tràn ngập không gian, tạo cảm giác khoáng đạt cho tất cả 7 hành khách.</p>\n<p>Đèn viền nội thất 30 màu điều chỉnh theo 4 chế độ không gian, Head-up Display chiếu thông tin lên kính chắn gió và hệ thống sạc không dây Qi chuẩn cho cả hàng ghế trước lẫn sau.</p>\n\n<h2>Thông tin đặt xe</h2>\n<p>Volkswagen Teramont X Platinum 2025 có giá bán 2.349.000.000 VNĐ. Với số lượng xe có hạn, quý khách có thể liên hệ trực tiếp showroom Volkswagen An Phú hoặc gọi <strong>098 105 8232</strong> để được tư vấn và đặt chỗ sớm nhất.</p>")
                    .build(),

                NewsPost.builder()
                    .slug("lai-thu-mien-phi")
                    .category("Khuyến mãi")
                    .title("Chương trình lái thử miễn phí tháng 4 – Trải nghiệm trước, quyết định sau")
                    .excerpt("Đăng ký lái thử toàn bộ dòng xe Volkswagen hoàn toàn miễn phí trong tháng 4/2025 tại showroom An Phú.")
                    .imageUrl("/images/news/news3.jpg")
                    .published(true)
                    .content("<p>Volkswagen An Phú triển khai chương trình lái thử miễn phí trong suốt tháng 4/2025, mang đến cơ hội trải nghiệm thực tế toàn bộ dòng xe Volkswagen trước khi đưa ra quyết định mua.</p>\n\n<h2>Các dòng xe tham gia chương trình</h2>\n<p>Khách hàng có thể đăng ký lái thử tất cả 5 mẫu xe đang có mặt tại showroom, bao gồm:</p>\n<ul>\n<li><strong>Tiguan 2025</strong> — SUV đô thị 5 chỗ, từ 1.699.000.000 VNĐ</li>\n<li><strong>Teramont 2025</strong> — SUV 7 chỗ rộng rãi, từ 2.199.000.000 VNĐ</li>\n<li><strong>Teramont X Platinum</strong> — SUV 7 chỗ cao cấp, từ 2.349.000.000 VNĐ</li>\n<li><strong>Touareg 2025</strong> — SUV flagship, từ 2.999.000.000 VNĐ</li>\n<li><strong>Viloran 2025</strong> — MPV executive 7 chỗ, từ 1.999.000.000 VNĐ</li>\n</ul>\n\n<h2>Quy trình đăng ký đơn giản</h2>\n<p>Khách hàng chỉ cần điền form đăng ký trực tuyến trên website hoặc gọi trực tiếp đến hotline <strong>098 105 8232</strong>. Chuyên viên tư vấn sẽ liên hệ xác nhận lịch hẹn trong vòng 24 giờ và hướng dẫn chuẩn bị giấy tờ cần thiết (CCCD và bằng lái xe hợp lệ).</p>\n\n<h2>Ưu đãi kèm theo</h2>\n<p>Khách hàng tham gia lái thử trong tháng 4 sẽ nhận được tư vấn cá nhân hóa từ chuyên gia Volkswagen, báo giá chi tiết và các chương trình hỗ trợ tài chính ưu đãi. Đặc biệt, khách hàng đặt cọc ngay sau buổi lái thử sẽ được tặng gói phụ kiện chính hãng trị giá lên đến 15.000.000 VNĐ.</p>\n<p>Chương trình áp dụng từ ngày 01/04/2025 đến hết ngày 30/04/2025. Số lượng suất lái thử mỗi ngày có hạn, quý khách vui lòng đăng ký sớm để chọn khung giờ phù hợp.</p>")
                    .build()
            );
            newsPostRepository.saveAll(posts);
            log.info("Seeded {} news posts", posts.size());
        }
    }
}
