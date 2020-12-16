package hr.example.treeapp;

public class Post {
    String ID_objava;
    String Korisnik_ID;
    String Datum_objave;
    double Latitude;
    double longitute;
    String Opis;
    String URL_slike;

    public Post(String ID_objava, String korisnik_ID, String datum_objave, double latitude, double longitute, String opis, String URL_slike) {
        this.ID_objava = ID_objava;
        Korisnik_ID = korisnik_ID;
        Datum_objave = datum_objave;
        Latitude = latitude;
        this.longitute = longitute;
        Opis = opis;
        this.URL_slike = URL_slike;
    }

    public String getID_objava() {
        return ID_objava;
    }

    public void setID_objava(String ID_objava) {
        this.ID_objava = ID_objava;
    }

    public String getKorisnik_ID() {
        return Korisnik_ID;
    }

    public void setKorisnik_ID(String korisnik_ID) {
        Korisnik_ID = korisnik_ID;
    }

    public String getDatum_objave() {
        return Datum_objave;
    }

    public void setDatum_objave(String datum_objave) {
        Datum_objave = datum_objave;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitute() {
        return longitute;
    }

    public void setLongitute(double longitute) {
        this.longitute = longitute;
    }

    public String getOpis() {
        return Opis;
    }

    public void setOpis(String opis) {
        Opis = opis;
    }

    public String getURL_slike() {
        return URL_slike;
    }

    public void setURL_slike(String URL_slike) {
        this.URL_slike = URL_slike;
    }
}
