package auth;

import java.util.Date;

public class User {
    public String uid;
    public String ime;
    public String prezime;
    public String email;
    public String profilnaSlika;
    public int uloga;
    public String korisnickoIme;
    public Date datumRodenja;
    public int bodovi;

    public User(){}

    User(String uid, String ime, String prezime, String email,
    String profilnaSlika, int uloga, String korisnickoIme, Date datumRodenja, int bodovi)
    {
        this.uid=uid;
        this.ime=ime;
        this.prezime=prezime;
        this.email=email;
        this.profilnaSlika=profilnaSlika;
        this.uloga=uloga;
        this.bodovi=bodovi;
        this.korisnickoIme=korisnickoIme;
        this.datumRodenja=datumRodenja;
    }

}
