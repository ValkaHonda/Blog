package blog.areas.user.bindingModels;

import javax.validation.constraints.NotNull;

public class ChangePassBindingModel {
    @NotNull
    private String currentPass;
    @NotNull
    private String newPass;
    @NotNull
    private String confirmPass;

    public String getCurrentPass() {
        return currentPass;
    }

    public void setCurrentPass(String currentPass) {
        this.currentPass = currentPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getConfirmPass() {
        return confirmPass;
    }

    public void setConfirmPass(String confirmPass) {
        this.confirmPass = confirmPass;
    }
}
