package com.awesomepark.app.views;


//import com.awesomepark.app.views.bookingadminpanel.BookingAdminPanelView;
import com.awesomepark.app.views.bookingadminpanel.BookingAdminPanelView;
import com.awesomepark.app.views.bookingform.BookingFormView;
import com.awesomepark.app.views.contacts.ContactsView;
import com.awesomepark.app.views.datagrid.DataGridView;
import com.awesomepark.app.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import org.vaadin.lineawesome.LineAwesomeIcon;


@PageTitle("AwesomePark")
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
                    TextColor.BODY);
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

            if (icon != null) {
                link.add(icon);
            }
            link.add(text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

    }

    public MainLayout() {
        addToNavbar(createHeaderContent());
    }

    private Component createHeaderContent() {
        Header header = new Header();
        header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

        Div layout = new Div();
        layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);
        Div layout1 = new Div();
        layout1.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

        H1 appName = new H1("AwesomePark");
        appName.addClassNames(Margin.Vertical.MEDIUM, Margin.End.AUTO, FontSize.XXXLARGE);
        layout.add(appName);


        Nav nav = new Nav();
        nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            list.add(menuItem);

        }

        header.add(layout, nav);
        return header;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //
                new MenuItemInfo("Главная", LineAwesomeIcon.HOME_SOLID.create(), MainView.class), //

                new MenuItemInfo("Записаться", LineAwesomeIcon.USER_CHECK_SOLID.create(), BookingFormView.class), //

                new MenuItemInfo("Таблица записи", LineAwesomeIcon.TABLE_SOLID.create(), DataGridView.class), //

                new MenuItemInfo("Админка", LineAwesomeIcon.COG_SOLID.create(), BookingAdminPanelView.class), //

                new MenuItemInfo("Контакты", LineAwesomeIcon.MAP_MARKED_SOLID.create(), ContactsView.class), //

        };
    }

}
