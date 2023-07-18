
## 1- property --> id, usurname, email, password, activationCode(String),
##    ERole(USER; ADMIN), EStatus(PENDING, ACTIVE, INACTIVE, DELETED, BANNED)
##    Bu sınıfın repository, service, controller' ını oluşturunuz.
##    Utility katmanını oluşturunuz, Crud metotları için gerekli repo paternini kurunuz.
##  2- Register için bir requestDto ve responseDto oluşturunuz.
##   Mapper katmanından yararlanarak AuthService' de register metodunu yazınız ve test ediniz.
##   requestDto --> username,email, password
##    responseDto --> id, username, activationCode
##  Register metodu yazılacak
##  3- (boolean)activateStatus metodunu yazınız. Bu metot için bir dto oluşturunuz. Dto --> id, activationCode
##  activateStatus metodu login metodundan önce çalışacaktır. Register olduğumuzda gelen kod burada girilerek kullanıcının durumu
##  ACTIVE olarak değiştirilir. Daha sonra login işlemi yapılabilir.
##  4- (boolean)login metodu yazınız. Login metodu için bir dto oluşturunuz. Dto --> username, password
##  Kullanıcının status' ü ACTIVE yapılmadıysa hata dönmelidir.
##  Exception katmanını projeye dahil ediniz.
##  5- OpenPeign için gerekli işlemleri yapınız. Bağımlılıkları ekleyiniz, bir dto(NewCreateUserRegisterDto) oluşturularak auth-service de yapılan 
##  register işleminin user-service de çalışmasını sağlayınız. OpenFeign için manager katmanını oluşturunuz.

##  Auth-Service deki activateStatus metodu kullanıldığında register olan kullanıcının durumu ACTIVE olarak değişmektedir.
##  Ancak bu değişiklik User-Profile service e aktarılan kullanıcının durumunda değişiklik yaratmamaktadır.
##  Bu durumu gidermek için activateStatus metodu feign client ile user-profile service e aktarılmalıdır.
##  Bunun için gerekli işlemleri yapınız.
##  UserManager'da yazacağınız activateStatus metodu yanlızca authId parametresi almalıdır.

##  6- UserProfile'daki eksik kalan bilgileri veya username, email gibi AuthService'den gelen bilgiler update edilebilmelidir.
##  Bu işlem için UserProfileUpdateDto adında bir oluşturarak gerekli işlemleri yapınız.
##  Bu bilgiler içerisinden username ve email bilgilerinin OpenFeign kullanılarak AuthService'e gönderilmessi gerekmektedir.
##  Bunun için de bir dto'ya ihtiyaç duyulabilir.'UpdateEmailOrUsernameRequestDto' oluşturup bu dto üzerinden veriler
##  AuthService'e gönderilmelidir.

## 7- Config server için gerekli işlemleri yapınız.
## config-server'ın application.yml' ını yazınız.
## config-repo adında bir paket oluşturarak içerisine auth-service ve userprofile-service' in ayarlarını giriniz
## userprofile ve auth service' de içi boşalan .yml dosyalarını config-server üzerinden çalıştıracak ayarları yazınız

## 8- Projenize Zipkin' in bağımlılıklarını ekleyiniz.
## Zipkin için bir imaj oluşturunuz ve gerekli ayarları projenizde yapınız.

## 9- UserService'de bir metot(findByUsername) yazmalıyız. Bu metot username'e göre veritabanından veriyi getirmelidir.
## büyük-küçük harf duyarlılığı ortadan kalkmalıdır. Metot için gerekli hata kontrollerini yapınız.
## Metottan gelen veriyi cacheleyiniz.

##  10- Veritabanındaki bütün kullanıcıları döndüren metodu cache'leyiniz. Veritabanında bir değişiklik olduğunda bu cache silinsin ve
##  yeni kullanıcılar veri setinde gösterilebilsin. Veriler tekrar cache'lenmiş olsun.

##  11- findByRole metodu için kayıt olunduğunda veya kullanıcı bilgileri güncellendiğinde cache'in silinmesi gerekiyor.
##  bunun için gerekli işlemleri yapınız.
##  findByRole için put işlemi userProfileService'de yapılamamaktadır. Bu işlemi AuthService'den deneyin. 
##  Gmail hesabı olmayanlar gmail hesabı açınız. Eğer varsa ikinci bir gmail hesabı açınız. İki adımlı doğrulamayı aktifleştiriniz.

## 12- mail-service için rabbitmq konfigürasyonnunu yazınız, rabbitmq ile gelen veriyi tüketmek için consumer ve model katmanlarını oluşturunuz.
## MailServicce sınıfı aracılığıyla bu bilgileri JavaMailsender sınıfına veriniz. ve Test ediniz.

## 13- utility katmanında GetAllData adında bir sınıf oluşturunuz. Bu sınıfın içerisinde initData() adında bir metot olacak.
## Bu metot manager katmanı aracılığıyla userProfileService' den bütün verileri çekecek ve elasticsearch' ün in-memory' sine kayıt edecek
## Bu metot program çalıştıırıldığında bir kere çalışacak ve bir daha çalışmayacak.
## Bu verilerin elasticsearch' e kayıt olduğunu görüntülemek için burada bir findAll metodu da olmalıdır.

## 14- AuthService' de yapılan kayıt işlemi(registerWithRabbitMq) verileri user-profile service' e gelmektedir.
## Buraya gelen veriler elastic-service' e gönderilmeli ve burada in-memory' ye kayıt edilmelidir.
## Bunun için user-profile service' deki config, rabbitmq ve service katmanında gerekli işlemleri yapınız.
## user-profile 'producer', elastic-service 'consumer' olmalıdır.

## 15- ElasticSearch'deki verilerin pageable edilmesi gerekmektedir. Bunun için gerekli işlemleri yapınız.
## Kullanacağınız metot findAll metodudur.

## 16- ChangePassword işleminin yapılması;
    Bunun için bir dto oluştur. token,oldPassword,newPassword
    oldPassword kontrol edilmelidir ve buna göre newPAssword gelen veriile set edilmelidir.
    bu işlemleri yapan metodun adı 'passwordChange(), boolean
    buradaki yapılan değişiklik auth-service' gönderilmelidir(openfeign)

## 17- Auth service de yapılan forgotPassword işleminde kullanıcının şifresi, üretilen yeni bir şifre ile değiştirilmektedir.
## Bu değişikliğin userprofile-service'e de aktarılması için openfeign yöntemi ile gerekli işlemleri gerçekleştiriniz.


# docker build --build-arg JAR_FILE=config-server-git/build/libs/config-server-git-v.0.0.1.jar --platform linux/amd64 -t emrekazanci/social-config-git-server:v.1.0 .