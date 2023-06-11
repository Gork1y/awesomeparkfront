package com.awesomepark.app.views.main;

import com.awesomepark.app.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Main")
@Route(value = "Main", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {

    public MainView() {
        setSpacing(false);

        Image img = new Image("images/awesome.png", "awesome park");
        img.setWidth("75%");
        img.setMaxHeight("auto");
        add(img);

        add(new Paragraph("скоро тут будет наикрутейшая круть, а пока вы можете:"));
        Button bookingButton = new Button("Записаться на каталку");
        bookingButton.addClassNames("large-button");
        bookingButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("bookings")));
        add(bookingButton);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
