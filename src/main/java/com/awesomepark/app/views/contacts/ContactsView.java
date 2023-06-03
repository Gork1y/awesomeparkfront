package com.awesomepark.app.views.contacts;

import com.awesomepark.app.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;

@PageTitle("Контакты")
@Route(value = "Contacts", layout = MainLayout.class)
public class ContactsView extends VerticalLayout {

    public ContactsView() {
        setSpacing(false);

        Image img = new Image("images/awesome.png", "awesome park");
        img.setWidth("75%");
        img.setMaxHeight("auto");
        add(img);

        H2 header = new H2("Мы находимся по адресу:\n" +
                "\n" +
                "г. Калининград, Московский проспект 375 (территория отеля \"Балтика\")\n" +
                "\n" +
                "для клиентов парка предусмотрен бесплатный паркинг");
        header.addClassNames(Margin.Top.XLARGE, Margin.Bottom.MEDIUM);
        add(header);
        add(new Paragraph("а еще тут может быть ваша реклама 🤗"));

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }
}
