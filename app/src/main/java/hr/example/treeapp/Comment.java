package hr.example.treeapp;

public class Comment {
    String Komentar_ID;
    String Korisnik_ID;
    String Tekst;

    public Comment(String komentar_ID, String korisnik_ID, String tekst) {
        Komentar_ID = komentar_ID;
        Korisnik_ID = korisnik_ID;
        Tekst = tekst;
    }

    public String getKomentar_ID() {
        return Komentar_ID;
    }

    public void setKomentar_ID(String komentar_ID) {
        Komentar_ID = komentar_ID;
    }

    public String getKorisnik_ID() {
        return Korisnik_ID;
    }

    public void setKorisnik_ID(String korisnik_ID) {
        Korisnik_ID = korisnik_ID;
    }

    public String getTekst() {
        return Tekst;
    }

    public void setTekst(String tekst) {
        Tekst = tekst;
    }
}
