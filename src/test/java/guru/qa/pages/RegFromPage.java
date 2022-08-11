package guru.qa.pages;

import com.codeborne.selenide.SelenideElement;
import guru.qa.pages.Component.Checker;
import guru.qa.pages.Component.Setter;
import guru.qa.restaker.Inf;
import org.openqa.selenium.Keys;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class RegFromPage {
    private Setter setInPage = new Setter();
    private Checker matchIt = new Checker();
    private SelenideElement
            firstNameInput = $("#firstName"),
            lastNameInput = $("#lastName"),
            emailInput = $("#userEmail"),
            genderInput = $("#genterWrapper"),
            phoneInput = $("#userNumber"),
            addressInput = $("#currentAddress"),
            stateInput = $("#react-select-3-input"),//$("#state").click();$("#stateCity-wrapper").$(byText("NCR")).click();
            cityInput = $("#react-select-4-input"),//$("#city").click();$("#stateCity-wrapper").$(byText("Delhi")).click();
            dateOfBirthIput = $("#dateOfBirthInput"),
            subjInput = $("#subjectsInput"),
            hobbiesInput = $("#hobbiesWrapper"),
            pictUpload = $("#uploadPicture"),
            submitButton = $("#submit");


    public RegFromPage openPage(){
        open("/automation-practice-form");
        executeJavaScript("$('footer').remove()");
        executeJavaScript("$('#fixedban').remove()");
        return this;
    }
    public RegFromPage fillPageForm(Inf data){
        setInPage
                .setAndEnt(firstNameInput,data.fname)
                .setAndEnt(lastNameInput,data.lname)
                .setAndEnt(emailInput,data.email);
        this.setGender(data.gender);
        setInPage.setAndEnt(phoneInput,data.phone);
        this
            .setDateOfBirth(data.birth)
            .setSubj(data.subj)
            .setHobbies(data.hobbies)
            .setPhoto(data.pict);
        setInPage
                .setAndEnt(addressInput,data.address)
                .setAndEnt(stateInput,data.state)
                .setAndEnt(cityInput,data.city);
        this.clickSubmit();
        return this;
    }

    private RegFromPage clickSubmit() {
        submitButton.scrollTo().click();
        return this;
    }

    private RegFromPage setPhoto(String pict) {
        pictUpload.uploadFile(new File("src/test/resources/"+pict));
        return this;
    }

    private RegFromPage setHobbies(String[] hobbies) {
        for (int i=0;i<hobbies.length;i++){
            hobbiesInput.find(byText(hobbies[i])).click();
        }
        return this;
    }

    private RegFromPage setSubj(String[] subj) {
        for (int i = 0; i<subj.length;i++){
            subjInput.setValue(subj[i]).pressEnter();
        }
        return this;
    }

    private RegFromPage setDateOfBirth(String datebirth) {
        dateOfBirthIput.sendKeys(Keys.CONTROL + "a");
        dateOfBirthIput.sendKeys(datebirth + Keys.ENTER);
        return this;
    }

    private RegFromPage setGender(String gender) {
        genderInput.find(byText(gender)).click();
        return this;
    }

    public RegFromPage checkPageFrom(Inf data){
        LocalDate time = LocalDate.parse(data.birth, DateTimeFormatter.ofPattern("MM.dd.yyyy"));
        String newTime = time.format(DateTimeFormatter.ofPattern("dd MMMM,yyyy",new Locale("en")));

        String strVersOfSubj = data.subj[0];
        for (int i=1;i<data.subj.length;i++){
            strVersOfSubj += ", "+data.subj[i];
        }

        String strVersOfHobbies = data.hobbies[0];
        for (int i=1;i<data.hobbies.length;i++){
            strVersOfHobbies += ", "+data.hobbies[i];
        }
        matchIt
                .checkRow("Student Name",data.fname+" "+data.lname)
                .checkRow("Student Email",data.email)
                .checkRow("Student Gender",data.gender)
                .checkRow("Student Phone",data.phone)
                .checkRow("Date of Birth",newTime)
                .checkRow("Subjects",strVersOfSubj)
                .checkRow("Hobbies",strVersOfHobbies)
                .checkRow("Photo",data.pict)
                .checkRow("Address",data.address)
                .checkRow("State and City",data.state+" "+data.city);
        return this;
    }
}
