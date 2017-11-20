package beans;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Locale;
import javax.faces.context.FacesContext;

@Named(value = "language")
@SessionScoped
public class language implements Serializable {
    
    public void english() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(Locale.ENGLISH);
    }
    
    public void polish() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.getViewRoot().setLocale(new Locale("pl"));
    }

    public language() {
    }
    
}
