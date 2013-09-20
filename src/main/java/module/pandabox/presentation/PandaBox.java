/*
 * @(#)PandaBox.java
 *
 * Copyright 2011 Instituto Superior Tecnico
 * Founding Authors: Pedro Santos
 * 
 *      https://fenix-ashes.ist.utl.pt/
 * 
 *   This file is part of the Pandabox Module.
 *
 *   The Pandabox Module is free software: you can
 *   redistribute it and/or modify it under the terms of the GNU Lesser General
 *   Public License as published by the Free Software Foundation, either version 
 *   3 of the License, or (at your option) any later version.
 *
 *   The Pandabox Module is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with the Pandabox Module. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package module.pandabox.presentation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;

import module.vaadin.ui.BennuTheme;
import pt.ist.bennu.core._development.PropertiesManager;
import pt.ist.bennu.core.domain.MyOrg;
import pt.ist.bennu.core.domain.Theme;
import pt.ist.bennu.core.domain.ThemeType;
import pt.ist.vaadinframework.annotation.EmbeddedComponent;
import pt.ist.vaadinframework.ui.EmbeddedComponentContainer;
import pt.ist.vaadinframework.ui.GridSystemLayout;
import pt.ist.vaadinframework.ui.PaginatedSorterViewer;
import pt.ist.vaadinframework.ui.PaginatedSorterViewer.ContentViewerFactory;

import com.vaadin.data.Container.Viewer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.UserError;
import com.vaadin.terminal.gwt.server.WebApplicationContext;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractSplitPanel;
import com.vaadin.ui.Accordion;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Form;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.InlineDateField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Link;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.Select;
import com.vaadin.ui.Slider;
import com.vaadin.ui.Slider.ValueOutOfBoundsException;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;

@EmbeddedComponent(path = { "box" })
/**
 * 
 * @author Pedro Santos
 * 
 */
public class PandaBox extends CustomComponent implements EmbeddedComponentContainer {
    @Override
    public boolean isAllowedToOpen(Map<String, String> arguments) {
	return true;
    }

    @Override
    public void setArguments(Map<String, String> arguments) {
    }

    private static final String LOREM_TEXT_ALL = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?";
    private static final String LOREM_TEXT_LARGE = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    private static final String LOREM_TEXT_SMALL = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.";
    private final String image = "../panda/img/black_square.gif";

    HorizontalSplitPanel root = new HorizontalSplitPanel();

    private class PageChangeListener implements ClickListener {
	@Override
	public void buttonClick(final ClickEvent event) {
	    final Button button = event.getButton();
	    final Component componentToSwitch = buttonComponentMap.get(button);
	    switchTo(componentToSwitch, button);
	}
    }

    VerticalLayout previewTabs;
    VerticalLayout compoundTabs;
    VerticalLayout bennuStylesTabs;

    private final Map<Button, Component> buttonComponentMap = new LinkedHashMap<Button, Component>();
    private Button shownTab = null;
    private final ClickListener tabChangeListener = new PageChangeListener();

    CssLayout previewArea = new CssLayout();
    Button editorToggle = new Button();

    public PandaBox() {
	initView();
	try {
	    buildComponentPreviews();
	} catch (ValueOutOfBoundsException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	showWelcomeScreen();
    }

    private void showWelcomeScreen() {
	CustomLayout welcome = new CustomLayout("welcome");
	welcome.setSizeFull();
	root.setSecondComponent(welcome);
    }

    private void initView() {
	setCompositionRoot(root);
	root.setSizeFull();
	root.setSplitPosition(15);
	root.setStyleName("small previews");

	previewArea.setWidth("100%");
	previewTabs = new VerticalLayout();
	previewTabs.setSizeFull();
	previewTabs.setHeight(null);

	compoundTabs = new VerticalLayout();
	compoundTabs.setSizeFull();
	compoundTabs.setHeight(null);

	bennuStylesTabs = new VerticalLayout();
	bennuStylesTabs.setSizeFull();
	bennuStylesTabs.setHeight(null);

	VerticalLayout menu = new VerticalLayout();
	menu.setSizeFull();
	menu.setStyleName("sidebar-menu");

	Button syncThemes = new Button("Sync Themes", new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		syncThemes();
	    }
	});
	menu.addComponent(syncThemes);
	menu.addComponent(new Label("Single Components"));
	menu.addComponent(previewTabs);
	menu.addComponent(new Label("Compound Styles"));
	menu.addComponent(compoundTabs);

	menu.addComponent(new Label("Bennu Styles"));
	menu.addComponent(bennuStylesTabs);

	root.setFirstComponent(menu);

	CssLayout toolbar = new CssLayout();
	toolbar.setWidth("100%");
	toolbar.setStyleName("toolbar");
	toolbar.addComponent(editorToggle);

	final Window downloadWindow = new Window("Download Theme");
	GridLayout l = new GridLayout(3, 2);
	l.setSizeUndefined();
	l.setMargin(true);
	l.setSpacing(true);
	downloadWindow.setContent(l);
	downloadWindow.setModal(true);
	downloadWindow.setResizable(false);
	downloadWindow.setCloseShortcut(KeyCode.ESCAPE, null);
	downloadWindow.addStyleName("opaque");
	Label caption = new Label("Theme Name");
	l.addComponent(caption);
	l.setComponentAlignment(caption, Alignment.MIDDLE_CENTER);
	final TextField name = new TextField();
	name.setValue("my-chameleon");
	name.addValidator(new RegexpValidator("[a-zA-Z0-9\\-_\\.]+", "Only alpha-numeric characters allowed"));
	name.setRequired(true);
	name.setRequiredError("Please give a name for the theme");
	downloadWindow.addComponent(name);
	Label info = new Label(
		"This is the name you will use to set the theme in your application code, i.e. <code>setTheme(\"my-cameleon\")</code>.",
		Label.CONTENT_XHTML);
	info.addStyleName("tiny");
	info.setWidth("200px");
	l.addComponent(info, 1, 1, 2, 1);

	Button download = new Button(null, new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		getApplication().getMainWindow().addWindow(downloadWindow);
		name.focus();
	    }
	});
	download.setDescription("Donwload the current theme");
	download.setIcon(new ThemeResource("download.png"));
	download.setStyleName("icon-only");
	toolbar.addComponent(download);

	menu.addComponent(toolbar);
	menu.setExpandRatio(toolbar, 1);
	menu.setComponentAlignment(toolbar, Alignment.BOTTOM_CENTER);

    }

    private void syncThemes() {
	File cssDir = new File(((WebApplicationContext) getApplication().getContext()).getBaseDirectory(), "CSS");
	File[] files = cssDir.listFiles(new FileFilter() {

	    @Override
	    public boolean accept(File file) {
		return file.isDirectory();
	    }
	});

	for (Theme theme : MyOrg.getInstance().getThemes()) {
	    if (!matchTheme(theme.getName(), files)) {
		Theme.deleteTheme(theme);
	    }
	}

	for (File directory : files) {
	    String themeName = directory.getName();
	    if (!Theme.isThemeAvailable(themeName)) {
		try {
		    Properties themeProperties = loadThemePropeties(themeName);
		    ThemeType type = ThemeType.valueOf(themeProperties.getProperty("theme.type"));
		    String description = themeProperties.getProperty("theme.description");
		    String screenshotFileName = themeProperties.getProperty("theme.screenshotFileName");
		    Theme.createTheme(themeName, description, type, screenshotFileName);
		} catch (IOException e) {
		    // Theme Configuration not readable, ignore.
		}
	    }
	}

    }

    private boolean matchTheme(String name, File[] files) {
	for (File file : files) {
	    if (file.getName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    private Properties loadThemePropeties(String themeName) throws IOException {
	return PropertiesManager.loadPropertiesFromFile(((WebApplicationContext) getApplication().getContext())
		.getBaseDirectory().getAbsolutePath() + "/CSS/" + themeName + "/theme.properties");
    }

    public void addTab(final VerticalLayout menu, final Component content, final String caption) {
	if (content == null || caption == null) {
	    throw new NullPointerException("Arguments may not be null");
	}

	final Button button = new NativeButton(caption, tabChangeListener);
	button.setWidth("100%");

	menu.addComponent(button);
	buttonComponentMap.put(button, content);
    }

    private void switchTo(final Component componentToSwitch, final Button button) {
	if (shownTab != null) {
	    shownTab.removeStyleName("tab-selected");
	}
	shownTab = button;
	if (shownTab != null) {
	    shownTab.addStyleName("tab-selected");
	}

	root.setSecondComponent(previewArea);
	previewArea.removeAllComponents();
	previewArea.addComponent(componentToSwitch);
    }

    private void buildComponentPreviews() throws ValueOutOfBoundsException {
	// Compound styles need to be added first so that basic style are shown
	// first
	addTab(bennuStylesTabs, getBennuInterface(), "Complete Interface");
	addTab(bennuStylesTabs, getVerticalLayout(), "Vertical Layout");
	addTab(bennuStylesTabs, getHorizontalLayout(), "Horizontal Layout");
	addTab(bennuStylesTabs, getGridSystemLayout(), "GridSystem Layout");
	addTab(bennuStylesTabs, getPaginationPage(), "Pagination Component");
	addTab(bennuStylesTabs, getSotisFrontPage(), "Sotis Front Page");
	addTab(bennuStylesTabs, getSystemErrorTester(), "System Error Test");

	addTab(compoundTabs, getCompoundButtons(), "Buttons");
	addTab(compoundTabs, getCompoundMenus(), "Menus");

	addTab(previewTabs, getLabelPreviews(), "Labels");
	addTab(previewTabs, getButtonPreviews(), "Buttons");
	addTab(previewTabs, getTextFieldPreviews(), "Text fields");
	addTab(previewTabs, getSelectPreviews(), "Selects");
	addTab(previewTabs, getDateFieldPreviews(), "Date fields");
	addTab(previewTabs, getSliderPreviews(), "Sliders");
	addTab(previewTabs, getPanelPreviews(), "Panels");
	addTab(previewTabs, getSplitPreviews(), "Split panels");
	addTab(previewTabs, getTabsheetPreviews(), "Tab sheets");
	addTab(previewTabs, getAccordionPreviews(), "Accordions");
	addTab(previewTabs, getTablePreviews(), "Tables");
	addTab(previewTabs, getProgressIndicatorPreviews(), "Progress indicators");
	addTab(previewTabs, getTreePreviews(), "Trees");
	addTab(previewTabs, getPopupViewPreviews(), "Popup views");
	addTab(previewTabs, getMenuBarPreviews(), "Menu bars");
	addTab(previewTabs, getWindowPreviews(), "Windows");
    }

    private Component getVerticalLayout() {
	HorizontalLayout controls = new HorizontalLayout();
	controls.setSpacing(true);
	controls.setStyleName("inline");
	final VerticalLayout main = new VerticalLayout();

	final VerticalLayout vl = new VerticalLayout();
	vl.setSpacing(true);
	vl.setMargin(true);
	vl.setCaption("Layout Caption");
	vl.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	vl.addComponent(createImageFixedHeight());
	vl.addComponent(createImageFixedHeight());
	vl.addComponent(createImageFixedHeight());

	controls.addComponent(new StyleController(vl, BennuTheme.LAYOUT_BIG, BennuTheme.LAYOUT_INSET, BennuTheme.LAYOUT_SECTION));
	controls.addComponent(new SpacingController(vl));
	controls.addComponent(new MarginController(vl));

	main.addComponent(controls);
	main.addComponent(vl);

	return main;
    }

    private Component getHorizontalLayout() {
	HorizontalLayout controls = new HorizontalLayout();
	controls.setSpacing(true);
	controls.setStyleName("inline");
	final VerticalLayout main = new VerticalLayout();

	final HorizontalLayout hl = new HorizontalLayout();
	hl.setSpacing(true);
	hl.setMargin(true);
	hl.setCaption("Layout Caption");
	hl.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	hl.addComponent(createImageFixedHeight());
	hl.addComponent(createImageFixedHeight());
	hl.addComponent(createImageFixedHeight());

	controls.addComponent(new StyleController(hl, BennuTheme.LAYOUT_BIG, BennuTheme.LAYOUT_INSET, BennuTheme.LAYOUT_SECTION));
	controls.addComponent(new SpacingController(hl));
	controls.addComponent(new MarginController(hl));

	main.addComponent(controls);
	main.addComponent(hl);

	return main;
    }

    private Component getGridSystemLayout() {
	Integer index = 0;
	GridSystemLayout gsl = new GridSystemLayout();
	gsl.setMargin(false);
	for (Integer i : new Integer[] { 1, 2, 4, 8, 16 }) {
	    System.out.println("puti " + i);
	    for (Integer j = i; j <= 16; j += i) {
		index++;
		gsl.setCell(index.toString(), i, createImage());
	    }
	}
	for (Integer i : new Integer[] { 2, 4, 6, 4 }) {
	    index++;
	    gsl.setCell(index.toString(), i, createImage());
	}
	for (Integer j = 1; j <= 16; j++) {
	    index++;
	    gsl.setCell(index.toString(), 0, j, 16 - j, createImage());
	}

	for (Integer j = 1; j <= 16; j++) {
	    index++;
	    gsl.setCell(index.toString(), 16 - j, j, 0, createImage());
	}

	for (Integer j = 2; j <= 14; j += 2) {
	    index++;
	    Integer fixed = (16 - j) / 2;
	    gsl.setCell(index.toString(), fixed, j, fixed, createImage());
	}

	for (Integer j = 14; j >= 2; j -= 2) {
	    index++;
	    Integer fixed = (16 - j) / 2;
	    gsl.setCell(index.toString(), fixed, j, fixed, createImage());
	}

	return gsl;
    }

    // private Component createImage() {
    // final Embedded embedded = new Embedded(null, new ThemeResource(image));
    // embedded.setSizeFull();
    // return embedded;
    // }

    private Button createImage() {
	final Button button = new Button("zen");
	button.setSizeFull();
	return button;
    }

    private Button createImageFixedHeight() {
	final Button button = new Button("zen");
	button.setWidth("100%");
	button.setHeight("50px");
	return button;
    }

    private Component getBennuInterface() {
	GridSystemLayout gsl = new GridSystemLayout();
	gsl.setMargin(false);

	Label lblBigTitle = new Label("A Big Title (H1)", Label.CONTENT_TEXT);
	lblBigTitle.setStyleName(BennuTheme.LABEL_H1);

	Label lblBigText = new Label(LOREM_TEXT_LARGE, Label.CONTENT_TEXT);

	gsl.setCell("big_title", 16, lblBigTitle);
	gsl.setCell("big_text", 16, lblBigText);

	Label lblOneLink = new Label("<a href='http://www.google.com'>Label Link : One link a day keeps the doctor away</a>",
		Label.CONTENT_XHTML);
	gsl.setCell("label_one_link", 16, lblOneLink);

	Button btOneLink = new Button("Button Link: One link a day keeps the doctor away", new ClickListener() {

	    @Override
	    public void buttonClick(ClickEvent event) {
		event.getButton().getWindow().showNotification("You just clicked a button link!");
	    }
	});

	btOneLink.setStyleName(BaseTheme.BUTTON_LINK);
	gsl.setCell("button_one_link", 16, btOneLink);

	Label lblTitleH2 = new Label("A Not So Big Title (H2)", Label.CONTENT_TEXT);
	lblTitleH2.setStyleName(BennuTheme.LABEL_H2);
	gsl.setCell("not_so_big_title", 16, lblTitleH2);

	Label lblSmallText = new Label(LOREM_TEXT_SMALL, Label.CONTENT_TEXT);
	gsl.setCell("small_text", 16, lblSmallText);

	Table table = new Table();
	table.setSizeFull();
	table.setPageLength(0);
	table.addContainerProperty("Name", String.class, "");
	table.addContainerProperty("Age", Integer.class, "");
	table.addContainerProperty("Nickname", String.class, "");

	table.addItem(new Object[] { "Giacomo Guilizzoni", 34, "Peidi" }, 1);
	table.addItem(new Object[] { "Giodp Jack Guilizzoni", 4, "The Guids" }, 2);
	table.addItem(new Object[] { "Marco Botton", 31, "" }, 3);
	table.addItem(new Object[] { "Mariah Maciachlan", 35, "Patata" }, 4);
	table.addItem(new Object[] { "Valerie Libery WOW! Division", 23, "Val" }, 5);
	table.addItem(new Object[] { "Guido Master lorem ipsum lorem ipsum lorem ipsum lorem ipsum lorem ipsum", 99,
		"Booya Master" }, 6);

	gsl.setCell("table", 16, table);

	Label lblTitleH3 = new Label("A Getting Smaller Title (H3)", Label.CONTENT_TEXT);
	lblTitleH3.setStyleName(BennuTheme.LABEL_H3);
	gsl.setCell("getting_smaller_title", 16, lblTitleH3);

	Panel panelLeft = new Panel();
	panelLeft.setScrollable(true);
	panelLeft.setSizeFull();
	panelLeft.setHeight("145px");
	panelLeft.addComponent(new Label(LOREM_TEXT_ALL));

	Panel panelRight = new Panel();
	panelRight.addComponent(new Label(LOREM_TEXT_LARGE));

	gsl.setCell("panel_left", 8, panelLeft);
	gsl.setCell("panel_right", 8, panelRight);

	Label lblTitleH4 = new Label("A Smaller Title (H4)", Label.CONTENT_TEXT);
	lblTitleH4.setStyleName(BennuTheme.LABEL_H4);
	gsl.setCell("smaller_title", 16, lblTitleH4);

	Label lblTextb4Form = new Label(LOREM_TEXT_SMALL, Label.CONTENT_TEXT);
	gsl.setCell("txtB4Form", 0, 8, 8, lblTextb4Form);

	Form form = new Form();
	form.setSizeFull();
	form.addField("form_label", new TextField("Form Label"));
	form.addField("large_form_label", new DateField("Large Form Label"));
	form.addField("much_larger_form_label", new Select("Much Larger Form Label"));
	form.addField("something_diff", new TextArea("And now for something completely different"));

	final OptionGroup checkboxes = new OptionGroup("Checkboxes fun");
	checkboxes.setMultiSelect(true);
	checkboxes.addItem("not selected");
	checkboxes.addItem("selected");
	checkboxes.select("selected");
	checkboxes.addItem("disabled");
	checkboxes.setItemEnabled("disabled", false);
	checkboxes.addItem("disabled selected");
	checkboxes.select("disabled selected");
	checkboxes.setItemEnabled("disabled selected", false);

	form.addField("checkboxes", checkboxes);

	final OptionGroup radiobuttons = new OptionGroup("Radio on/off");
	radiobuttons.addItem("option 1(selected)");
	radiobuttons.select("option 1(selected)");
	radiobuttons.addItem("option 2");
	radiobuttons.addItem("option 3 (disabled)");
	radiobuttons.setItemEnabled("option 3 (disabled)", false);

	radiobuttons.addItem("option 4 (disabled and selected)");
	radiobuttons.select("option 4 (disabled and selected)");
	radiobuttons.setItemEnabled("option 4 (disabled and selected)", false);

	form.addField("radiobuttons", radiobuttons);

	form.getFooter().addComponent(new Button("Submit the info"));
	form.getFooter().addComponent(new Button("Cancel the info"));

	final Panel rightFormPanel = new Panel();
	rightFormPanel.setScrollable(true);
	rightFormPanel.setSizeFull();
	rightFormPanel.setHeight("400px");
	rightFormPanel.addComponent(new Label(LOREM_TEXT_ALL, Label.CONTENT_TEXT));

	gsl.setCell("form", 12, form);
	gsl.setCell("rightFormPanel", 4, rightFormPanel);

	return gsl;
    }

    private Layout getPaginationPage() {
	PaginatedSorterViewer viewer = new PaginatedSorterViewer(new ContentViewerFactory() {
	    @Override
	    public Viewer makeViewer() {
		Table table = new Table();
		table.setPageLength(0);
		table.setSizeFull();
		return table;
	    }
	});
	viewer.setSorter(new Object[] { "name", "phone" }, new boolean[] { true, true }, new String[] { "Nome", "Telefone" });
	viewer.setGrouper(new Object[] { "age" }, new boolean[] { true }, new String[] { "Idade" });
	viewer.setPagination();
	viewer.setFilter(new Object[] { "name" }, new String[] { "Nome" });

	IndexedContainer stuff = new IndexedContainer();
	stuff.addContainerProperty("name", String.class, null);
	stuff.addContainerProperty("phone", Integer.class, null);
	stuff.addContainerProperty("age", Integer.class, null);
	stuff.addContainerProperty("id", Integer.class, null);
	for (int i = 0; i < 500; i++) {
	    Object itemId = stuff.addItem();
	    stuff.getItem(itemId).getItemProperty("name").setValue(UUID.randomUUID().toString());
	    stuff.getItem(itemId).getItemProperty("phone").setValue(new Random().nextInt(899999999) + 100000000);
	    stuff.getItem(itemId).getItemProperty("age").setValue(new Random().nextInt(25));
	    stuff.getItem(itemId).getItemProperty("id").setValue(i);
	}
	viewer.setContainerDataSource(stuff);

	return viewer;
    }

    private Layout getSotisFrontPage() {
	GridSystemLayout layout = new GridSystemLayout(12);
	Label title = new Label("O <strong>SOTIS</strong> é o Repositório Institucional do Instituto Superior Técnico.",
		Label.CONTENT_XHTML);
	title.addStyleName(BennuTheme.LABEL_BIG);
	layout.setCell("title", 12, title);

	Label description = new Label(
		"Aqui poderá encontrar artigos publicados em <a href=\"\">Revistas</a>, <a href=\"\">Conferências</a>, <a href=\"\">Livros</a>, <a href=\"\">Manuais</a> e <a href=\"\">Outros</a>, categorizados por <a href=\"\">Unidades de Investigação</a> e <a href=\"\">Unidades Académicas</a>.",
		Label.CONTENT_XHTML);
	description.addStyleName(BennuTheme.LABEL_BIG);
	layout.setCell("description", 12, description);

	VerticalLayout searchPanel = new VerticalLayout();
	layout.setCell("search", 2, 8, 2, searchPanel);
	searchPanel.addStyleName("big");
	searchPanel.addStyleName("inset");
	searchPanel.setMargin(true);
	searchPanel.setSpacing(true);
	HorizontalLayout searchForm = new HorizontalLayout();
	searchPanel.addComponent(searchForm);
	searchForm.setSpacing(true);
	searchForm.setWidth("100%");

	TextField searchText = new TextField();
	searchForm.addComponent(searchText);
	searchText.setInputPrompt("Introduza o termo a pesquisar");
	searchText.setWidth("100%");

	Button searchSubmit = new Button("Pesquisar");
	searchForm.addComponent(searchSubmit);
	searchSubmit.addStyleName(BennuTheme.BUTTON_DEFAULT);
	searchForm.setExpandRatio(searchText, 1f);

	Link advanced = new Link("advanced search", null);
	searchPanel.addComponent(advanced);

	Panel browseByType = new Panel("Publicações por Tipo");
	browseByType.addStyleName(BennuTheme.PANEL_LIGHT);
	layout.setCell("type", 4, browseByType);

	Panel browseByDept = new Panel("Publicações por Departamento");
	browseByDept.addStyleName(BennuTheme.PANEL_LIGHT);
	layout.setCell("dept", 4, browseByDept);

	Panel contacts = new Panel("Contactos");
	contacts.addStyleName(BennuTheme.PANEL_LIGHT);
	layout.setCell("contacts", 4, contacts);

	return layout;
    }

    private static Layout getSystemErrorTester() {
	VerticalLayout layout = new VerticalLayout();
	layout.setSpacing(true);

	Button button = new Button("click me to show error", new ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		throw new Error("badabum");
	    }
	});
	layout.addComponent(button);

	final TextField txtCaption = new TextField("caption");
	txtCaption.setValue("Notification Caption");

	final TextField txtMessage = new TextField("message");
	txtMessage.setValue("Notification Message");

	final TextField txtDelay = new TextField("delay (msec)");
	txtDelay.setValue("2000");

	final Select position = new Select("Position");
	position.setNullSelectionAllowed(false);

	final int[] positions = { Window.Notification.POSITION_BOTTOM_LEFT, Window.Notification.POSITION_BOTTOM_RIGHT,
		Window.Notification.POSITION_CENTERED, Window.Notification.POSITION_CENTERED_BOTTOM,
		Window.Notification.POSITION_CENTERED_TOP, Window.Notification.POSITION_TOP_LEFT,
		Window.Notification.POSITION_TOP_RIGHT };

	final String[] posLabels = { "BOTTOM_LEFT", " BOTTOM_RIGHT", "CENTERED", "CENTERED_BOTTOM", "CENTERED_TOP", "TOP_LEFT",
		"TOP_RIGHT" };

	for (int i = 0; i < positions.length; i++) {
	    int pos = positions[i];
	    position.addItem(pos);
	    position.setItemCaption(pos, posLabels[i]);
	}

	position.select(position.getContainerDataSource().getItem(0));

	layout.addComponent(txtCaption);
	layout.addComponent(txtMessage);
	layout.addComponent(txtDelay);
	layout.addComponent(position);

	HorizontalLayout hl = new HorizontalLayout();
	hl.setSpacing(true);

	final int[] types = { Window.Notification.TYPE_ERROR_MESSAGE, Window.Notification.TYPE_HUMANIZED_MESSAGE,
		Window.Notification.TYPE_TRAY_NOTIFICATION, Window.Notification.TYPE_WARNING_MESSAGE };
	final String[] labels = { "error", "humanized", "tray", "warning" };
	for (int i = 0; i < types.length; i++) {
	    final int type = types[i];
	    Button btNotif = new Button(labels[i], new ClickListener() {

		@Override
		public void buttonClick(ClickEvent event) {
		    Window.Notification notif = new Window.Notification((String) txtCaption.getValue(),
			    (String) txtMessage.getValue(), type);
		    notif.setPosition((Integer) position.getValue());
		    notif.setDelayMsec(Integer.parseInt((String) txtDelay.getValue()));
		    event.getButton().getApplication().getMainWindow().showNotification(notif);
		}
	    });
	    hl.addComponent(btNotif);
	}

	layout.addComponent(hl);
	return layout;
    }

    private Layout getLabelPreviews() {
	Layout grid = getPreviewLayout("Labels");

	Label label = new Label("<h4>Paragraph Header</h4>Plain text, lorem ipsum dolor sit amet consectetur amit.",
		Label.CONTENT_XHTML);
	label.setWidth("200px");
	grid.addComponent(label);

	label = new Label("Big plain text, lorem ipsum dolor sit amet consectetur amit.");
	label.setWidth("200px");
	label.setStyleName("big");
	grid.addComponent(label);

	label = new Label("Small plain text, lorem ipsum dolor sit amet consectetur amit.");
	label.setWidth("200px");
	label.setStyleName("small");
	grid.addComponent(label);

	label = new Label("Tiny plain text, lorem ipsum dolor sit amet consectetur amit.");
	label.setWidth("200px");
	label.setStyleName("tiny");
	grid.addComponent(label);

	label = new Label("<h1>Top Level Header</h1>", Label.CONTENT_XHTML);
	label.setSizeUndefined();
	grid.addComponent(label);
	label.setDescription("Label.addStyleName(\"h1\");<br>or<br>new Label(\"&lt;h1&gt;Top Level Header&lt;/h1&gt;\", Label.CONTENT_XHTML);");

	label = new Label("<h2>Second Header</h2>", Label.CONTENT_XHTML);
	label.setSizeUndefined();
	grid.addComponent(label);
	label.setDescription("Label.addStyleName(\"h2\");<br>or<br>new Label(\"&lt;h2&gt;Second Header&lt;/h2&gt;\", Label.CONTENT_XHTML);");

	label = new Label("<h3>Subtitle</h3>", Label.CONTENT_XHTML);
	label.setSizeUndefined();
	grid.addComponent(label);
	label.setDescription("Label.addStyleName(\"h3\");<br>or<br>new Label(\"&lt;h3&gt;Subtitle&lt;/h3&gt;\", Label.CONTENT_XHTML);");

	label = new Label("<h4>Paragraph Header</h4>Plain text, lorem ipsum dolor sit amet consectetur amit.",
		Label.CONTENT_XHTML);
	label.setWidth("200px");
	label.setStyleName("color");
	grid.addComponent(label);

	label = new Label("Big plain text, lorem ipsum dolor sit amet consectetur amit.");
	label.setWidth("200px");
	label.setStyleName("big color");
	grid.addComponent(label);

	label = new Label("Small plain text, lorem ipsum dolor sit amet consectetur amit.");
	label.setWidth("200px");
	label.setStyleName("small color");
	grid.addComponent(label);

	label = new Label("Tiny plain text, lorem ipsum dolor sit amet consectetur amit.");
	label.setWidth("200px");
	label.setStyleName("tiny color");
	grid.addComponent(label);

	label = new Label("Top Level Header");
	label.setSizeUndefined();
	label.setStyleName("h1 color");
	grid.addComponent(label);

	label = new Label("Second Header");
	label.setSizeUndefined();
	label.setStyleName("h2 color");
	grid.addComponent(label);

	label = new Label("Subtitle");
	label.setSizeUndefined();
	label.setStyleName("h3 color");
	grid.addComponent(label);

	label = new Label("Warning text, lorem ipsum dolor sit.");
	label.setStyleName("warning");
	grid.addComponent(label);

	label = new Label("Error text, lorem ipsum dolor.");
	label.setStyleName("error");
	grid.addComponent(label);

	label = new Label("Big warning text");
	label.setStyleName("big warning");
	grid.addComponent(label);

	label = new Label("Big error text");
	label.setStyleName("big error");
	grid.addComponent(label);

	label = new Label("Loading text...");
	label.setStyleName("h3 loading");
	grid.addComponent(label);

	label = new Label("1");
	label.setSizeUndefined();
	label.addStyleName("numeral");
	label.addStyleName("green");
	grid.addComponent(label);

	label = new Label("2");
	label.setSizeUndefined();
	label.addStyleName("numeral");
	label.addStyleName("yellow");
	grid.addComponent(label);

	label = new Label("3");
	label.setSizeUndefined();
	label.addStyleName("numeral");
	label.addStyleName("blue");
	grid.addComponent(label);

	label = new Label("1");
	label.setSizeUndefined();
	label.addStyleName("numeral");
	label.addStyleName("green");
	label.addStyleName("big");
	grid.addComponent(label);

	label = new Label("2");
	label.setSizeUndefined();
	label.addStyleName("numeral");
	label.addStyleName("yellow");
	label.addStyleName("big");
	grid.addComponent(label);

	label = new Label("3");
	label.setSizeUndefined();
	label.addStyleName("numeral");
	label.addStyleName("blue");
	label.addStyleName("big");
	grid.addComponent(label);

	return grid;
    }

    private Layout getButtonPreviews() {
	Layout grid = getPreviewLayout("Buttons");

	Button button = new Button("Button");
	grid.addComponent(button);

	button = new Button("Default");
	button.setStyleName("default");
	grid.addComponent(button);

	button = new Button("Small");
	button.setStyleName("small");
	grid.addComponent(button);

	button = new Button("Small Default");
	button.setStyleName("small default");
	grid.addComponent(button);

	button = new Button("Big");
	button.setStyleName("big");
	grid.addComponent(button);

	button = new Button("Big Default");
	button.setStyleName("big default");
	grid.addComponent(button);

	button = new Button("Disabled");
	button.setEnabled(false);
	grid.addComponent(button);

	button = new Button("Disabled default");
	button.setEnabled(false);
	button.setStyleName("default");
	grid.addComponent(button);

	button = new Button("Link style");
	button.setStyleName(BaseTheme.BUTTON_LINK);
	grid.addComponent(button);

	button = new Button("Disabled link");
	button.setStyleName(BaseTheme.BUTTON_LINK);
	button.setEnabled(false);
	grid.addComponent(button);

	button = new Button("120px overflows out of the button");
	button.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	button.setWidth("120px");
	grid.addComponent(button);

	button = new Button("Small");
	button.setStyleName("small");
	button.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	grid.addComponent(button);

	button = new Button("Big");
	button.setStyleName("big");
	button.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	grid.addComponent(button);

	button = new Button("Big Default");
	button.setStyleName("big default");
	button.setIcon(new ThemeResource("../runo/icons/32/document-txt.png"));
	grid.addComponent(button);

	button = new Button("Big link");
	button.setStyleName(BaseTheme.BUTTON_LINK + " big");
	button.setIcon(new ThemeResource("../runo/icons/32/document.png"));
	grid.addComponent(button);

	button = new Button("Borderless");
	button.setStyleName("borderless");
	button.setIcon(new ThemeResource("../runo/icons/32/note.png"));
	grid.addComponent(button);

	button = new Button("Borderless icon on top");
	button.setStyleName("borderless icon-on-top");
	button.setIcon(new ThemeResource("../runo/icons/32/note.png"));
	grid.addComponent(button);

	button = new Button("Icon on top");
	button.setStyleName("icon-on-top");
	button.setIcon(new ThemeResource("../runo/icons/32/users.png"));
	grid.addComponent(button);

	button = new Button("Wide Default");
	button.setStyleName("wide default");
	grid.addComponent(button);

	button = new Button("Wide");
	button.setStyleName("wide");
	grid.addComponent(button);

	button = new Button("Tall");
	button.setStyleName("tall");
	grid.addComponent(button);

	button = new Button("Wide, Tall & Big");
	button.setStyleName("wide tall big");
	grid.addComponent(button);

	button = new Button("Icon on right");
	button.setStyleName("icon-on-right");
	button.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	grid.addComponent(button);

	button = new Button("Big icon");
	button.setStyleName("icon-on-right big");
	button.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	grid.addComponent(button);

	button = new Button("Toggle (down)");
	button.addListener(new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		if (event.getButton().getStyleName().endsWith("down")) {
		    event.getButton().removeStyleName("down");
		} else {
		    event.getButton().addStyleName("down");
		}
	    }
	});
	button.addStyleName("down");
	grid.addComponent(button);
	button.setDescription(button.getDescription() + "<br><strong>Stylename switching logic must be done separately</strong>");

	button = new Button();
	button.addListener(new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		if (event.getButton().getStyleName().endsWith("down")) {
		    event.getButton().removeStyleName("down");
		} else {
		    event.getButton().addStyleName("down");
		}
	    }
	});
	button.addStyleName("icon-only");
	button.addStyleName("down");
	button.setIcon(new ThemeResource("../runo/icons/16/user.png"));
	grid.addComponent(button);
	button.setDescription(button.getDescription() + "<br><strong>Stylename switching logic must be done separately</strong>");

	Link l = new Link("Link: vaadin.com", new ExternalResource("http://vaadin.com"));
	grid.addComponent(l);

	l = new Link("Link: vaadin.com", new ExternalResource("http://vaadin.com"));
	l.setIcon(new ThemeResource("../runo/icons/32/globe.png"));
	grid.addComponent(l);

	return grid;
    }

    private Layout getTextFieldPreviews() {
	Layout grid = getPreviewLayout("Text fields");

	TextField tf = new TextField();
	tf.setValue("Text field");
	grid.addComponent(tf);

	tf = new TextField();
	tf.setValue("Small field");
	tf.setStyleName("small");
	grid.addComponent(tf);

	tf = new TextField();
	tf.setValue("Big field");
	tf.setStyleName("big");
	tf.setComponentError(new UserError("Test error"));
	grid.addComponent(tf);

	tf = new TextField();
	tf.setInputPrompt("Search field");
	tf.setStyleName("search");
	grid.addComponent(tf);

	tf = new TextField();
	tf.setInputPrompt("Small search");
	tf.setStyleName("search small");
	grid.addComponent(tf);

	tf = new TextField();
	tf.setInputPrompt("Big search");
	tf.setStyleName("search big");
	grid.addComponent(tf);

	tf = new TextField("Error");
	tf.setComponentError(new UserError("Test error"));
	grid.addComponent(tf);

	tf = new TextField();
	tf.setInputPrompt("Error");
	tf.setComponentError(new UserError("Test error"));
	grid.addComponent(tf);

	tf = new TextField();
	tf.setInputPrompt("Small error");
	tf.setStyleName("small");
	tf.setComponentError(new UserError("Test error"));
	grid.addComponent(tf);

	TextArea ta = new TextArea();
	ta.setInputPrompt("Multiline");
	ta.setRows(4);
	grid.addComponent(ta);

	ta = new TextArea();
	ta.setInputPrompt("Small multiline");
	ta.setStyleName("small");
	ta.setRows(4);
	grid.addComponent(ta);

	ta = new TextArea();
	ta.setInputPrompt("Big multiline");
	ta.setStyleName("big");
	ta.setRows(4);
	grid.addComponent(ta);

	return grid;
    }

    private Layout getPanelPreviews() {
	Layout grid = getPreviewLayout("Panels");

	Panel panel = new DemoPanel("Panel");
	panel.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	grid.addComponent(panel);

	panel = new DemoPanel();
	grid.addComponent(panel);

	panel = new DemoPanel("Borderless Panel");
	panel.setStyleName(BennuTheme.PANEL_BORDERLESS);
	grid.addComponent(panel);

	panel = new DemoPanel();
	panel.setStyleName(BennuTheme.PANEL_BORDERLESS);
	grid.addComponent(panel);

	panel = new DemoPanel("Light panel");
	panel.setStyleName(BennuTheme.PANEL_LIGHT);
	panel.setIcon(new ThemeResource("../runo/icons/16/document.png"));
	grid.addComponent(panel);

	panel = new DemoPanel();
	panel.setStyleName(BennuTheme.PANEL_LIGHT);
	grid.addComponent(panel);

	panel = new DemoPanel("Borderless Light");
	panel.addStyleName(BennuTheme.PANEL_BORDERLESS);
	panel.addStyleName(BennuTheme.PANEL_LIGHT);
	grid.addComponent(panel);

	panel = new DemoPanel();
	panel.addStyleName(BennuTheme.PANEL_BORDERLESS);
	panel.addStyleName(BennuTheme.PANEL_LIGHT);
	grid.addComponent(panel);

	return grid;
    }

    private Layout getSplitPreviews() {
	Layout grid = getPreviewLayout("Split panels");

	AbstractSplitPanel panel = new VerticalSplitPanel();
	panel.setWidth("230px");
	panel.setHeight("130px");
	grid.addComponent(panel);

	panel = new VerticalSplitPanel();
	panel.setWidth("230px");
	panel.setHeight("130px");
	panel.setStyleName("small");
	grid.addComponent(panel);

	panel = new HorizontalSplitPanel();
	panel.setWidth("230px");
	panel.setHeight("130px");
	grid.addComponent(panel);

	panel = new HorizontalSplitPanel();
	panel.setWidth("230px");
	panel.setHeight("130px");
	panel.setStyleName("small");
	grid.addComponent(panel);

	return grid;
    }

    private Layout getDateFieldPreviews() {
	Layout grid = getPreviewLayout("Date fields");

	Date dateValue = null;
	try {
	    dateValue = new SimpleDateFormat("yyyyMMdd", Locale.US).parse("20110101");
	} catch (ParseException e) {
	    e.printStackTrace();
	}

	DateField date = new DateField();
	date.setValue(dateValue);
	date.setResolution(DateField.RESOLUTION_MIN);
	grid.addComponent(date);

	date = new DateField("Small date");
	date.setValue(dateValue);
	date.setResolution(DateField.RESOLUTION_YEAR);
	date.setStyleName("small");
	grid.addComponent(date);

	date = new DateField("Big date");
	date.setValue(dateValue);
	date.setResolution(DateField.RESOLUTION_MONTH);
	date.setStyleName("big");
	grid.addComponent(date);

	date = new InlineDateField("Inline date");
	date.setValue(dateValue);
	date.setResolution(DateField.RESOLUTION_DAY);
	grid.addComponent(date);

	date = new InlineDateField("Inline date, year resolution");
	date.setValue(dateValue);
	date.setResolution(DateField.RESOLUTION_MONTH);
	grid.addComponent(date);

	return grid;
    }

    Layout getTabsheetPreviews() {
	Layout grid = getPreviewLayout("Tab sheets");

	TabSheet tabs = new DemoTabsheet(false);
	grid.addComponent(tabs);

	tabs = new DemoTabsheet(true);
	grid.addComponent(tabs);

	tabs = new DemoTabsheet(false);
	tabs.setStyleName("borderless");
	grid.addComponent(tabs);

	tabs = new DemoTabsheet(true);
	tabs.setStyleName("borderless open-only-closable");
	grid.addComponent(tabs);

	return grid;
    }

    Layout getAccordionPreviews() {
	Layout grid = getPreviewLayout("Accordions");

	Accordion tabs = new DemoAccordion(false);
	grid.addComponent(tabs);

	// tabs = new DemoAccordion(true);
	// grid.addComponent(tabs);

	tabs = new DemoAccordion(false);
	tabs.setStyleName("borderless");
	grid.addComponent(tabs);

	tabs = new DemoAccordion(true);
	tabs.setStyleName("opaque");
	grid.addComponent(tabs);

	tabs = new DemoAccordion(true);
	tabs.setStyleName("opaque borderless");
	grid.addComponent(tabs);

	return grid;
    }

    Layout getSliderPreviews() throws ValueOutOfBoundsException {
	Layout grid = getPreviewLayout("Sliders");

	Slider s = new Slider();
	s.setWidth("200px");
	s.setValue(50);
	grid.addComponent(s);

	s = new Slider();
	s.setOrientation(Slider.ORIENTATION_VERTICAL);
	s.setHeight("70px");
	s.setValue(50);
	grid.addComponent(s);

	return grid;
    }

    Layout getTablePreviews() {
	Layout grid = getPreviewLayout("Tables");

	Table t = getDemoTable(null);
	grid.addComponent(t);

	t = getDemoTable("small");
	grid.addComponent(t);

	t = getDemoTable("big");
	grid.addComponent(t);

	t = getDemoTable("striped");
	grid.addComponent(t);

	t = getDemoTable("small striped");
	grid.addComponent(t);

	t = getDemoTable("big striped");
	grid.addComponent(t);

	t = getDemoTable("strong");
	grid.addComponent(t);

	t = getDemoTable("small strong");
	grid.addComponent(t);

	t = getDemoTable("big strong");
	grid.addComponent(t);

	t = getDemoTable("borderless");
	grid.addComponent(t);

	t = getDemoTable("striped");
	t.setColumnHeaderMode(Table.COLUMN_HEADER_MODE_HIDDEN);
	t.setCaption(t.getCaption() + ", hidden headers");
	grid.addComponent(t);

	t = getDemoTable("tstyle1");
	grid.addComponent(t);
	t = getDemoTable("tstyle2");
	grid.addComponent(t);
	t = getDemoTable("tstyle3");
	grid.addComponent(t);

	return grid;
    }

    Layout getSelectPreviews() {
	Layout grid = getPreviewLayout("Selects");

	ComboBox combo = new ComboBox();
	addSelectItems(combo, true, 100);
	grid.addComponent(combo);

	combo = new ComboBox();
	addSelectItems(combo, true, 100);
	combo.setStyleName("small");
	grid.addComponent(combo);

	combo = new ComboBox();
	addSelectItems(combo, true, 100);
	combo.setStyleName("big");
	grid.addComponent(combo);

	combo = new ComboBox();
	addSelectItems(combo, false, 5);
	combo.setStyleName("search");
	combo.setInputPrompt("Search combo");
	grid.addComponent(combo);

	combo = new ComboBox();
	addSelectItems(combo, false, 5);
	combo.setStyleName("small search");
	combo.setInputPrompt("Small search combo");
	grid.addComponent(combo);

	combo = new ComboBox();
	addSelectItems(combo, false, 5);
	combo.setStyleName("big search");
	combo.setInputPrompt("Big search combo");
	grid.addComponent(combo);

	NativeSelect s = new NativeSelect();
	addSelectItems(s, true, 10);
	grid.addComponent(s);

	s = new NativeSelect();
	addSelectItems(s, true, 10);
	s.setStyleName("small");
	grid.addComponent(s);

	s = new NativeSelect();
	addSelectItems(s, true, 10);
	s.setStyleName("big");
	grid.addComponent(s);

	combo = new ComboBox();
	addSelectItems(combo, false, 5);
	combo.setInputPrompt("Just click me");
	combo.setStyleName("select-button");
	// Must always specify width
	combo.setWidth("150px");
	grid.addComponent(combo);
	combo.setDescription(combo.getDescription()
		+ "<br><strong>You must always specify an explicit width for a combobox with this style, otherwise it will not work</strong>");

	return grid;
    }

    static void addSelectItems(AbstractSelect s, boolean selectFirst, int num) {
	s.setNullSelectionAllowed(false);
	for (int i = 0; i < num; i++) {
	    s.addItem("Item " + i);
	}
	if (selectFirst) {
	    s.select(s.getItemIds().iterator().next());
	}
    }

    Layout getProgressIndicatorPreviews() {
	Layout grid = getPreviewLayout("Progress Indicators");

	ProgressIndicator pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setCaption("Normal");
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setCaption("ProgressIndicator.setStyleName(\"small\")");
	pi.setStyleName("small");
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setCaption("ProgressIndicator.setStyleName(\"big\")");
	pi.setStyleName("big");
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setIndeterminate(true);
	pi.setCaption("Indeterminate, style \"bar\"");
	pi.setStyleName("bar");
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setIndeterminate(true);
	pi.setCaption("Indeterminate, style \"small bar\"");
	pi.setStyleName("small bar");
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setIndeterminate(true);
	pi.setCaption("Indeterminate, style \"big bar\"");
	pi.setStyleName("big bar");
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setCaption("Indeterminate, default style");
	pi.setIndeterminate(true);
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setCaption("Indeterminate, style \"big\"");
	pi.setStyleName("big");
	pi.setIndeterminate(true);
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setCaption("Disabled");
	pi.setEnabled(false);
	grid.addComponent(pi);

	pi = new ProgressIndicator(0.5f);
	pi.setPollingInterval(100000000);
	pi.setCaption("Indeterminate bar disabled");
	pi.setIndeterminate(true);
	pi.setStyleName("bar");
	pi.setEnabled(false);
	grid.addComponent(pi);

	return grid;
    }

    Tree tree;

    Layout getTreePreviews() {
	Layout grid = getPreviewLayout("Trees");
	tree = new Tree();
	tree.setImmediate(true);
	// we'll use a property for caption instead of the item id ("value"),
	// so that multiple items can have the same caption
	tree.addContainerProperty("caption", String.class, "");
	tree.setItemCaptionMode(AbstractSelect.ITEM_CAPTION_MODE_PROPERTY);
	tree.setItemCaptionPropertyId("caption");
	for (int i = 1; i <= 3; i++) {
	    final Object id = addCaptionedItem("Division " + i, null);
	    tree.expandItem(id);
	    addCaptionedItem("Team A", id);
	    addCaptionedItem("Team B", id);
	    tree.setItemIcon(id, new ThemeResource("../runo/icons/16/folder.png"));
	}
	grid.addComponent(tree);
	return grid;
    }

    Layout getPopupViewPreviews() {
	Layout grid = getPreviewLayout("Popup views");

	Label content = new Label("Simple popup content");
	content.setSizeUndefined();
	PopupView pv = new PopupView("Default popup", content);
	grid.addComponent(pv);

	return grid;
    }

    Layout getMenuBarPreviews() {
	Layout grid = getPreviewLayout("Menu bars");

	MenuBar menubar = new MenuBar();
	final MenuBar.MenuItem file = menubar.addItem("File", null);
	final MenuBar.MenuItem newItem = file.addItem("New", null);
	file.addItem("Open file...", null);
	file.addSeparator();

	newItem.addItem("File", null);
	newItem.addItem("Folder", null);
	newItem.addItem("Project...", null);

	file.addItem("Close", null);
	file.addItem("Close All", null);
	file.addSeparator();

	file.addItem("Save", null);
	file.addItem("Save As...", null);
	file.addItem("Save All", null);

	final MenuBar.MenuItem edit = menubar.addItem("Edit", null);
	edit.addItem("Undo", null);
	edit.addItem("Redo", null).setEnabled(false);
	edit.addSeparator();

	edit.addItem("Cut", null);
	edit.addItem("Copy", null);
	edit.addItem("Paste", null);
	edit.addSeparator();

	final MenuBar.MenuItem find = edit.addItem("Find/Replace", null);

	// Actions can be added inline as well, of course
	find.addItem("Google Search", null);
	find.addSeparator();
	find.addItem("Find/Replace...", null);
	find.addItem("Find Next", null);
	find.addItem("Find Previous", null);

	final MenuBar.MenuItem view = menubar.addItem("View", null);
	view.addItem("Show/Hide Status Bar", null);
	view.addItem("Customize Toolbar...", null);
	view.addSeparator();

	view.addItem("Actual Size", null);
	view.addItem("Zoom In", null);
	view.addItem("Zoom Out", null);

	grid.addComponent(menubar);

	return grid;
    }

    Layout getWindowPreviews() {
	Layout grid = getPreviewLayout("Windows");

	Button win = new Button("Open normal sub-window", new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		getApplication().getMainWindow().addWindow(new Window("Normal window"));
	    }
	});
	grid.addComponent(win);
	win.setDescription("new Window()");

	win = new Button("Open opaque sub-window", new Button.ClickListener() {
	    @Override
	    public void buttonClick(ClickEvent event) {
		Window w = new Window("Window.addStyleName(\"opaque\")");
		w.addStyleName("opaque");
		getApplication().getMainWindow().addWindow(w);
	    }
	});
	grid.addComponent(win);
	win.setDescription("Window.addStyleName(\"opaque\")");

	return grid;
    }

    private Object addCaptionedItem(String caption, Object parent) {
	// add item, let tree decide id
	final Object id = tree.addItem();
	// get the created item
	final Item item = tree.getItem(id);
	// set our "caption" property
	final Property p = item.getItemProperty("caption");
	p.setValue(caption);
	if (parent != null) {
	    tree.setChildrenAllowed(parent, true);
	    tree.setParent(id, parent);
	    tree.setChildrenAllowed(id, false);
	}
	return id;
    }

    class DemoPanel extends Panel {
	DemoPanel() {
	    super();
	    setWidth("230px");
	    setHeight("120px");
	    addComponent(new Label(
		    "<h4>Panel content</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin malesuada volutpat vestibulum. Quisque elementum quam sed sem ultrices lobortis. Pellentesque non ligula ac dolor posuere tincidunt sed eu mi. Integer mattis fringilla nulla, ut cursus mauris scelerisque eu. Etiam bibendum placerat euismod. Nam egestas adipiscing orci sed tristique. Sed vitae enim nisi. Sed ac vehicula ipsum. Nulla quis quam nisi. Proin interdum lacus ipsum, at tristique nibh. Curabitur at ipsum sem. Donec venenatis aliquet neque, sit amet cursus lectus condimentum et. In mattis egestas erat, non cursus metus consectetur ac. Pellentesque eget nisl tellus.",
		    Label.CONTENT_XHTML));
	}

	DemoPanel(String caption) {
	    this();
	    setCaption(caption);
	}

	@Override
	public void setStyleName(String style) {
	    super.setStyleName(style);
	    removeAllComponents();
	    Label l = new Label(
		    "<h4>"
			    + style
			    + " panel content</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin malesuada volutpat vestibulum. Quisque elementum quam sed sem ultrices lobortis. Pellentesque non ligula ac dolor posuere tincidunt sed eu mi. Integer mattis fringilla nulla, ut cursus mauris scelerisque eu. Etiam bibendum placerat euismod. Nam egestas adipiscing orci sed tristique. Sed vitae enim nisi. Sed ac vehicula ipsum. Nulla quis quam nisi. Proin interdum lacus ipsum, at tristique nibh. Curabitur at ipsum sem. Donec venenatis aliquet neque, sit amet cursus lectus condimentum et. In mattis egestas erat, non cursus metus consectetur ac. Pellentesque eget nisl tellus.",
		    Label.CONTENT_XHTML);
	    l.setDescription("Panel.setStyleName(\"" + style + "\")");
	    addComponent(l);
	}

    }

    class DemoTabsheet extends TabSheet {
	DemoTabsheet(boolean closable) {
	    super();
	    setWidth("230px");
	    setHeight("140px");
	    for (int i = 1; i < 4; i++) {
		VerticalLayout l = new VerticalLayout();
		l.setMargin(true);
		Tab t = addTab(l);
		t.setCaption("Tab " + i);
		t.setClosable(closable);
		if (i == 1) {
		    l.addComponent(new Label(
			    "<h4>Tab sheet content</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin malesuada volutpat vestibulum. Quisque elementum quam sed sem ultrices lobortis. Pellentesque non ligula ac dolor posuere tincidunt sed eu mi. Integer mattis fringilla nulla, ut cursus mauris scelerisque eu. Etiam bibendum placerat euismod. Nam egestas adipiscing orci sed tristique. Sed vitae enim nisi. Sed ac vehicula ipsum. Nulla quis quam nisi. Proin interdum lacus ipsum, at tristique nibh. Curabitur at ipsum sem. Donec venenatis aliquet neque, sit amet cursus lectus condimentum et. In mattis egestas erat, non cursus metus consectetur ac. Pellentesque eget nisl tellus.",
			    Label.CONTENT_XHTML));
		}
		if (i == 3) {
		    t.setIcon(new ThemeResource("../runo/icons/16/document.png"));
		}
	    }
	}

	@Override
	public void setStyleName(String style) {
	    super.setStyleName(style);
	    Label l = new Label(
		    "<h4>"
			    + style
			    + " tab sheet content</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin malesuada volutpat vestibulum.",
		    Label.CONTENT_XHTML);
	    l.setDescription("TabSheet.setStyleName(\"" + style + "\")");
	    ((VerticalLayout) getSelectedTab()).removeAllComponents();
	    ((VerticalLayout) getSelectedTab()).addComponent(l);
	}
    }

    class DemoAccordion extends Accordion {
	DemoAccordion(boolean closable) {
	    super();
	    setWidth("70%");
	    setHeight("160px");
	    for (int i = 1; i < 5; i++) {
		VerticalLayout l = new VerticalLayout();
		l.setMargin(true);
		Tab t = addTab(l);
		t.setCaption("Sheet " + i);
		t.setClosable(closable);
		if (i == 1) {
		    l.addComponent(new Label(
			    "<h4>Accordion content</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin malesuada volutpat vestibulum. Quisque elementum quam sed sem ultrices lobortis. Pellentesque non ligula ac dolor posuere tincidunt sed eu mi. Integer mattis fringilla nulla, ut cursus mauris scelerisque eu. Etiam bibendum placerat euismod. Nam egestas adipiscing orci sed tristique. Sed vitae enim nisi. Sed ac vehicula ipsum. Nulla quis quam nisi. Proin interdum lacus ipsum, at tristique nibh. Curabitur at ipsum sem. Donec venenatis aliquet neque, sit amet cursus lectus condimentum et. In mattis egestas erat, non cursus metus consectetur ac. Pellentesque eget nisl tellus.",
			    Label.CONTENT_XHTML));
		}
		if (i == 3) {
		    t.setIcon(new ThemeResource("../runo/icons/16/document.png"));
		}
	    }
	}

	@Override
	public void setStyleName(String style) {
	    super.setStyleName(style);
	    Label l = new Label(
		    "<h4>"
			    + style
			    + " accordion content</h4>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin malesuada volutpat vestibulum.",
		    Label.CONTENT_XHTML);
	    l.setDescription("Accordion.setStyleName(\"" + style + "\")");
	    ((VerticalLayout) getSelectedTab()).removeAllComponents();
	    ((VerticalLayout) getSelectedTab()).addComponent(l);
	}
    }

    public Table getDemoTable(String style) {
	Table t = new Table();
	t.setWidth("250px");
	t.setPageLength(5);
	t.setSelectable(true);
	t.setColumnCollapsingAllowed(true);
	t.setColumnReorderingAllowed(true);

	if (style != null) {
	    t.setStyleName(style);
	    t.setCaption("Table.addStyleName(\"" + style + "\")");
	}

	t.addContainerProperty("First", String.class, null);
	t.addContainerProperty("Second", String.class, null);
	t.addContainerProperty("Third", String.class, null);

	for (int j = 1; j < 100; j++) {
	    t.addItem(new Object[] { "Foo " + j, "Bar " + j, "Lorem " + j }, j);
	}

	t.setColumnIcon("Third", new ThemeResource("../runo/icons/16/document.png"));
	t.select(1);

	return t;
    }

    Layout getCompoundButtons() {
	Layout grid = getPreviewLayout("Compound Buttons");

	Label title = new Label("Segment");
	title.setStyleName("h1");
	grid.addComponent(title);
	((GridLayout) grid).newLine();

	Label segments = new Label(
		"The segment control is just a set of buttons inside a HorizontalLayout. Use the structure shown on the right, <strong>and remember that you need to implement all logic yourself</strong>. This theme just provides suitable stylenames for you to use.",
		Label.CONTENT_XHTML);
	grid.addComponent(segments);
	segments = new Label(
		"HorizontalLayout.setStyleName(\"segment\") and .addStyleName(\"segment-alternate\")\n  +  Button.addStyleName(\"first\") and .addStyleName(\"down\")\n  +  Button\n\t...\n  +  Button.addStyleName(\"last\")",
		Label.CONTENT_PREFORMATTED);
	((GridLayout) grid).addComponent(segments, 1, 1, 2, 1);

	Segment segment = new Segment();
	segment.setCaption("Segment");
	Button b = new Button("One");
	b.setStyleName("down");
	b.setIcon(new ThemeResource("../runo/icons/16/document-txt.png"));
	segment.addButton(b).addButton(new Button("Two")).addButton(new Button("Three")).addButton(new Button("Four"));
	grid.addComponent(segment);

	segment = new Segment();
	segment.addStyleName("segment-alternate");
	segment.setCaption("Segment (alternate)");
	b = new Button("One");
	b.setStyleName("down");
	b.setIcon(new ThemeResource("../runo/icons/16/document-txt.png"));
	segment.addButton(b).addButton(new Button("Two")).addButton(new Button("Three")).addButton(new Button("Four"));
	grid.addComponent(segment);

	segment = new Segment();
	segment.setCaption("Small segment");
	b = new Button("Apples");
	b.setStyleName("small");
	b.addStyleName("down");
	segment.addButton(b);
	b = new Button("Oranges");
	b.setStyleName("small");
	segment.addButton(b);
	b = new Button("Bananas");
	b.setStyleName("small");
	segment.addButton(b);
	b = new Button("Grapes");
	b.setStyleName("small");
	segment.addButton(b);
	grid.addComponent(segment);

	return grid;
    }

    Layout getCompoundMenus() {
	Layout grid = getPreviewLayout("Compound Menus");

	Label title = new Label("Sidebar Menu");
	title.setStyleName("h1");
	grid.addComponent(title);
	((GridLayout) grid).newLine();

	Label menus = new Label(
		"<strong>The sidebar menu</strong> control is just a set of labels and buttons inside a CssLayout or a VerticalLayout. Use the structure shown on the right, <strong>and remember that you need to implement all logic yourself</strong>. This theme just provides suitable stylenames for you to use.<br><br>You can also use the <a href=\"http://vaadin.com/forum/-/message_boards/message/119172\">DetachedTabs add-on</a> inside the sidebar-menu, it will style automatically.<br><br><strong>Note: only NativeButtons are styled inside the menu, normal buttons are left untouched.</strong>",
		Label.CONTENT_XHTML);
	grid.addComponent(menus);
	menus = new Label(
		"CssLayout.setStyleName(\"sidebar-menu\")\n  +  Label\n  +  NativeButton\n  +  NativeButton\n\t...\n  +  Label\n  +  DetachedTabs\n\t...",
		Label.CONTENT_PREFORMATTED);
	grid.addComponent(menus);

	SidebarMenu sidebar = new SidebarMenu();
	sidebar.setWidth("200px");
	sidebar.addComponent(new Label("Fruits"));
	NativeButton b = new NativeButton("Apples");
	b.setIcon(new ThemeResource("../runo/icons/16/note.png"));
	sidebar.addButton(b);
	sidebar.setSelected(b);
	sidebar.addButton(new NativeButton("Oranges"));
	sidebar.addButton(new NativeButton("Bananas"));
	sidebar.addButton(new NativeButton("Grapes"));
	sidebar.addComponent(new Label("Vegetables"));
	sidebar.addButton(new NativeButton("Tomatoes"));
	sidebar.addButton(new NativeButton("Cabbages"));
	sidebar.addButton(new NativeButton("Potatoes"));
	sidebar.addButton(new NativeButton("Carrots"));
	grid.addComponent(sidebar);
	((GridLayout) grid).setColumnExpandRatio(0, 1);
	((GridLayout) grid).setColumnExpandRatio(1, 1);

	title = new Label("Toolbar");
	title.setStyleName("h1");
	grid.addComponent(title);
	((GridLayout) grid).newLine();

	CssLayout toolbars = new CssLayout();

	menus = new Label(
		"<strong>Toolbar</strong> is a simple CssLayout with a stylename. It provides the background and a little padding for its contents. Normally you will want to put buttons inside it, but segment controls fit in nicely as well.",
		Label.CONTENT_XHTML);
	grid.addComponent(menus);
	menus = new Label("CssLayout.setStyleName(\"toolbar\")", Label.CONTENT_PREFORMATTED);
	grid.addComponent(menus);

	CssLayout toolbar = new CssLayout();
	toolbar.setStyleName("toolbar");
	toolbar.setWidth("300px");

	Button b2 = new Button("Action");
	b2.setStyleName("small");
	toolbar.addComponent(b2);

	Segment segment = new Segment();
	segment.addStyleName("segment-alternate");
	b2 = new Button("Apples");
	b2.setStyleName("small");
	b2.addStyleName("down");
	segment.addButton(b2);
	b2 = new Button("Oranges");
	b2.setStyleName("small");
	segment.addButton(b2);
	toolbar.addComponent(segment);

	b2 = new Button("Notes");
	b2.setStyleName("small borderless");
	b2.setIcon(new ThemeResource("../runo/icons/16/note.png"));
	toolbar.addComponent(b2);
	toolbars.addComponent(toolbar);

	toolbar = new CssLayout();
	toolbar.setStyleName("toolbar");
	toolbar.setWidth("300px");

	b2 = new Button("Action");
	b2.setIcon(new ThemeResource("../runo/icons/32/document.png"));
	b2.setStyleName("borderless");
	toolbar.addComponent(b2);

	b2 = new Button("Action 2");
	b2.setStyleName("borderless");
	b2.setIcon(new ThemeResource("../runo/icons/32/user.png"));
	toolbar.addComponent(b2);

	b2 = new Button("Action 3");
	b2.setStyleName("borderless");
	b2.setIcon(new ThemeResource("../runo/icons/32/note.png"));
	toolbar.addComponent(b2);
	toolbars.addComponent(toolbar);

	grid.addComponent(toolbars);

	return grid;
    }

    GridLayout getPreviewLayout(String caption) {
	GridLayout grid = new GridLayout(3, 1) {
	    @Override
	    public void addComponent(Component c) {
		super.addComponent(c);
		setComponentAlignment(c, "center middle");
		if (c.getStyleName() != "") {
		    ((AbstractComponent) c).setDescription(c.getClass().getSimpleName() + ".addStyleName(\"" + c.getStyleName()
			    + "\")");
		} else {
		    ((AbstractComponent) c).setDescription("new " + c.getClass().getSimpleName() + "()");
		}
	    }
	};
	grid.setWidth("100%");
	grid.setSpacing(true);
	grid.setMargin(true);
	grid.setCaption(caption);
	grid.setStyleName("preview-grid");
	return grid;
    }

}
