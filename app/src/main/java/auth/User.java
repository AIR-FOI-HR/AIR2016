package auth;

import java.util.Date;

public class User {
    public String uid;
    public String ime;
    public String prezime;
    public String email;
    public String profilnaSlika;
    public long uloga;
    public String korisnickoIme;
    public String datumRodenja;
    public long bodovi;

    public User(){}

    User(String korime){
        this.korisnickoIme = korime;
    }

    public User(String uid, String ime, String prezime, String email,
    String profilnaSlika, long uloga, String korisnickoIme, String datumRodenja, long bodovi)
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
