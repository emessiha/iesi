package io.metadew.iesi.cockpit.define.environment;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import io.metadew.iesi.cockpit.MainLayout;
import io.metadew.iesi.metadata.definition.Environment;

@Route(value = "environments", layout = MainLayout.class)
public class EnvironmentView extends HorizontalLayout
        implements HasUrlParameter<String> {

	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "environments";
    private EnvironmentGrid grid;
    private EnvironmentForm form;
    private TextField filter;

    private EnvironmentLogic viewLogic = new EnvironmentLogic(this);
    private Button newEnvironment;

    private EnvironmentDataProvider dataProvider = new EnvironmentDataProvider();

    public EnvironmentView() {
        setSizeFull();
        HorizontalLayout topLayout = createTopBar();

        grid = new EnvironmentGrid();
        grid.setDataProvider(dataProvider);
        grid.asSingleSelect().addValueChangeListener(
                event -> viewLogic.rowSelected(event.getValue()));

        form = new EnvironmentForm(viewLogic);

        VerticalLayout barAndGridLayout = new VerticalLayout();
        barAndGridLayout.add(topLayout);
        barAndGridLayout.add(grid);
        barAndGridLayout.setFlexGrow(1, grid);
        barAndGridLayout.setFlexGrow(0, topLayout);
        barAndGridLayout.setSizeFull();
        barAndGridLayout.expand(grid);

        add(barAndGridLayout);
        add(form);

        viewLogic.init();
    }

    public HorizontalLayout createTopBar() {
        filter = new TextField();
        filter.setPlaceholder("Filter name or description");
        // Apply the filter to grid's data provider. TextField value is never null
        filter.addValueChangeListener(event -> dataProvider.setFilter(event.getValue()));
        filter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        newEnvironment = new Button("New Environment");
        newEnvironment.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        newEnvironment.setIcon(VaadinIcon.PLUS_CIRCLE.create());
        newEnvironment.addClickListener(click -> viewLogic.newEnvironment());
        // CTRL+N will create a new window which is unavoidable
        newEnvironment.addClickShortcut(Key.KEY_N, KeyModifier.ALT);

        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        topLayout.add(filter);
        topLayout.add(newEnvironment);
        topLayout.setVerticalComponentAlignment(Alignment.START, filter);
        topLayout.expand(filter);
        return topLayout;
    }

    public void showError(String msg) {
        Notification.show(msg);
    }

    public void showSaveNotification(String msg) {
        Notification.show(msg);
    }

    public void setNewEnvironmentEnabled(boolean enabled) {
        newEnvironment.setEnabled(enabled);
    }

    public void clearSelection() {
        grid.getSelectionModel().deselectAll();
    }

    public void selectRow(Environment row) {
        grid.getSelectionModel().select(row);
    }

    public Environment getSelectedRow() {
        return grid.getSelectedRow();
    }

    public void updateEnvironment(Environment Environment) {
        dataProvider.save(Environment);
    }

    public void removeEnvironment(Environment Environment) {
        dataProvider.delete(Environment);
    }

    public void editEnvironment(Environment environment) {
        showForm(environment != null);
        form.editEnvironment(environment);
    }

    public void showForm(boolean show) {
        form.setVisible(show);

        /* FIXME The following line should be uncommented when the CheckboxGroup
         * issue is resolved. The category CheckboxGroup throws an
         * IllegalArgumentException when the form is disabled.
         */
        //form.setEnabled(show);
    }

    @Override
    public void setParameter(BeforeEvent event,
                             @OptionalParameter String parameter) {
        viewLogic.enter(parameter);
    }
}
