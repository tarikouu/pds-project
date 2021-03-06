package app.views.simulations;

import app.controllers.FixedRateSimulationControllerClient;
import app.models.Customer;
import app.models.Insurance;
import app.models.LoanType;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

public class FixedRateSimulationView {

    // JPanel that contains all the components
    private JPanel panel;
    
    // components of the simulation frame
    private JButton btnProspect;
    private JButton btnCustomer;
    private JButton btnCreateSimulation;
    private JButton btnSearchSimulation;
    private JTextField txtFieldCustomer;
    private JButton btnSearch;
    private JComboBox cbCustomer;
    private JComboBox cbLoanType = null;
    private JComboBox cbInsurance;
    private JLabel lblTotalRate;
    private JTextField txtFieldRate;
    private JButton btnSimulate;
    private JButton btnCancel;
    private JTextField txtFieldWording;
    private JButton btnSave;
    private JTable tblSimulations;
    private DefaultTableModel mdlSimulations;
    private JButton btnEditSimulation;
    private JButton btnChangeParameters;
    private JTextField txtFieldAmount;
    private JTextField txtFieldDuration;
    private JButton btnBackToCustomerMenu;
    private JButton btnBackToCustomerSearch;
    private JButton btnBackToStart;
    private JButton btnUpdateSimulation;
    
    // GridBagConstraints to set the components locations
    private GridBagConstraints gc;

    // controller for the fixed rate credit simulation
    private FixedRateSimulationControllerClient controller;

    private String mode; // defines if the user is searching/editing a simulation or creating a simulation
    private boolean editParams, layoutChange;
    
    public FixedRateSimulationView(FixedRateSimulationControllerClient c, JPanel p) {
        // set the panel and its layout
        panel = p;
        
        // prepare the JPanel to the addition of the components
        panel.setVisible(false);
        panel.removeAll();
        
        // assign a controller to the view
        controller = c;
        
        setUIFont (new javax.swing.plaf.FontUIResource("Verdana",Font.PLAIN,22));
        
        // set the panel layout as a GridBagLayout
        panel.setLayout(new GridBagLayout());
        
        // configure the GridBagConstraints
        gc = new GridBagConstraints();
        gc.weightx = 0;
        gc.weighty = 1;
        gc.insets = new Insets(10, 10, 10, 10);
        gc.anchor = GridBagConstraints.LINE_START;
        
        btnProspect = new JButton("Simulation pour un prospect");
        btnProspect.addActionListener(new BtnProspectListener());
        btnCustomer = new JButton("Rechercher un client");
        btnCustomer.addActionListener(new BtnCustomerListener());
        
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(btnCustomer, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(btnProspect, gc);
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        editParams = false;
        layoutChange = false;
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    public void displayStartPage() {
        // prepare the JPanel to the addition of the components
        panel.setVisible(false);
        panel.removeAll();
        
        btnProspect = new JButton("Simulation pour un prospect");
        btnProspect.addActionListener(new BtnProspectListener());
        btnCustomer = new JButton("Rechercher un client");
        btnCustomer.addActionListener(new BtnCustomerListener());
        
        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(btnCustomer, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(btnProspect, gc);
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    // display the customer search interface
    public void displayCustomerSearch() {
        // prepare the JPanel to the addition of the components
        panel.setVisible(false);
        panel.removeAll();
        
        // initialisation of the components
        cbCustomer = new JComboBox();
        txtFieldCustomer = new JTextField(20);
        btnSearch = new JButton("Rechercher");
        btnSearch.addActionListener(new BtnSearchListener());
        btnBackToStart = new JButton("Annuler");
        btnBackToStart.addActionListener(new BtnBackToStartListener());
        
        // add components to the panel using the GridBagLayout and GridBagConstraints
        //gc.anchor = GridBagConstraints.LINE_START;

        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Nom du client : "), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        panel.add(txtFieldCustomer, gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(btnSearch, gc);
        
        gc.gridx = 1;
        gc.gridy = 1;
        panel.add(btnBackToStart, gc);
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    public void displayCustomers() {
        boolean found;

        // prepare the JPanel to the addition of the components
        panel.setVisible(false);
        panel.removeAll();

        // remove the action listener to avoid events being triggered while the JComboBox is being filled
        for(ItemListener il : cbCustomer.getItemListeners()) {
            cbCustomer.removeItemListener(il);
        }

        // clear the results of the last search
        cbCustomer.removeAllItems();
        
        // initialisation of the components
        ArrayList<Customer> customersList = controller.getCustomers(txtFieldCustomer.getText());
        for (Customer c : customersList) {
            cbCustomer.addItem(c);
        }
        
        // check if customers were found
        found = !customersList.isEmpty();
        
        if (found) {
            cbCustomer.insertItemAt("", 0); // add blank first item in JComboBox
            cbCustomer.setSelectedIndex(0); // select the JComboBox blank field
            cbCustomer.addItemListener(new CbCustomerItemListener());
        }
        
        // add components to the panel using the GridBagLayout and GridBagConstraints
        //gc.anchor = GridBagConstraints.LINE_START;

        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Nom du client : "), gc);

        gc.gridx = 1;
        gc.gridy = 0;
        panel.add(txtFieldCustomer, gc);
        
        gc.gridx = 1;
        gc.gridy = 1;
        panel.add(btnSearch, gc);
        
        if (found) {
            gc.gridwidth = 2;
            gc.gridx = 0;
            gc.gridy = 2;
            panel.add(cbCustomer, gc);
            gc.gridwidth = 1;
        }
        else {
            JOptionPane.showMessageDialog(null,"Aucun client trouvé");
        }
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    public void displayMenu() {
        if(layoutChange) {
            panel.setLayout(new GridBagLayout());
            layoutChange = false;
        }
        
        // prepare the JPanel to the addition of the components
        panel.setVisible(false);
        panel.removeAll();
        
        // initialisation of the components
        btnSearchSimulation = new JButton("Consulter une simulation");
        btnSearchSimulation.addActionListener(new BtnSearchSimulationListener());
        btnCreateSimulation = new JButton("Nouvelle simulation");
        btnCreateSimulation.addActionListener(new BtnCreateSimulationListener());
        
        // add components to the panel using the GridBagLayout and GridBagConstraints
        //gc.anchor = GridBagConstraints.LINE_START;

        gc.gridx = 0;
        gc.gridy = 0;
        panel.add(new JLabel("Client : " + controller.getFirstName() + " " + controller.getLastName()), gc);
        
        gc.gridx = 0;
        gc.gridy = 1;
        panel.add(btnCreateSimulation, gc);
        
        gc.gridx = 1;
        gc.gridy = 1;
        panel.add(btnSearchSimulation, gc);
        
        gc.gridx = 1;
        gc.gridy = 2;
        btnBackToCustomerSearch = new JButton("Retour");
        btnBackToCustomerSearch.addActionListener(new BtnBackToCustomerSearchListener());
        panel.add(btnBackToCustomerSearch, gc);
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    public void displayCustomerSimulations() {
        layoutChange = true;
        panel.setLayout(new BoxLayout(panel,BoxLayout.PAGE_AXIS));
        
        // get the chosen customer's simulations
        mdlSimulations = controller.getSimulations(controller.getCustomerId(), controller.getLoanTypeId());
        
        // check if some simulations were found for the chosen customer
        boolean found = (mdlSimulations.getColumnCount() != 0);
        
        panel.setVisible(false);
        
        // if some simulations are found
        panel.removeAll();

        // initialisation of the components
        
        if (found) {
            tblSimulations = new JTable(mdlSimulations); // initialise the JTable with a DefaultTableModel given by the controller
            tblSimulations.removeColumn(tblSimulations.getColumn("Num"));
            tblSimulations.getTableHeader().setFont(new Font("Verdana",Font.PLAIN,18));
            tblSimulations.setFont(new Font("Verdana",Font.PLAIN,18));
        }
        
        // add components to the panel using multiple layouts
        panel.add(new JLabel("Client : " + controller.getFirstName() + " " + controller.getLastName()));

        Color col = panel.getBackground();
        
        JPanel pnl = new JPanel();
        pnl.setLayout(new FlowLayout());
        pnl.setBackground(col);
        
        pnl.add(new JLabel("Type de prêt :"));

        pnl.add(cbLoanType);
        
        panel.add(pnl);
        
        JPanel pnl2 = new JPanel();
        //pnl2.setLayout(new BoxLayout(pnl2,BoxLayout.LINE_AXIS));*
        pnl2.setLayout(new FlowLayout());
        pnl2.setBackground(col);
        // if some simulations are found
        if (found) {
            JScrollPane jsp = new JScrollPane(tblSimulations, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            Dimension d = tblSimulations.getPreferredSize();
            tblSimulations.setRowHeight(30);
            
            JPanel jp = new JPanel();
            jp.add(jsp);

            jp.setLayout(new GridLayout(1,1)); /* little trick ;) and believe me that this step is important to the automatic all columns resize! */
            panel.add(jp);
            resizeColumnWidth(tblSimulations);
            jsp.setPreferredSize(new Dimension(d.width,tblSimulations.getRowHeight()*tblSimulations.getRowCount()+1));

            btnEditSimulation = new JButton("Modifier cette simulation");
            btnEditSimulation.addActionListener(new BtnEditSimulationListener());
            pnl2.add(btnEditSimulation);
        }
        
        btnCancel = new JButton("Retour");
        btnCancel.addActionListener(new BtnBackToCustomerMenuListener());
        //panel.add(btnCancel, gc);
        pnl2.add(btnCancel);
        
        panel.add(pnl2);

        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
        
        if (!found) {
            JOptionPane.showMessageDialog(null,"Aucune simulation de type \"" + controller.getLoanTypeWording() + "\" pour ce client" );
        }
    }
        
    public void displayLoanTypes() {
        // for the GBC gridy
        int y = 0;
        
        // prepare the JPanel to the addition of the components
        panel.setVisible(false);
        panel.removeAll();

        // initialisation of the components
        // fill the JComboBox with the loan types list
        cbLoanType = new JComboBox();
        ArrayList<LoanType> loanTypesList = controller.getLoanTypes();
        System.out.println("lts :");
        for (LoanType lt : loanTypesList) {
            System.out.println("lt : " + lt.getId() + " " + lt.getWording() + " " + lt.getRate());
            cbLoanType.addItem(lt);
        }
        cbLoanType.insertItemAt("", 0); // add blank first item in JComboBox
        cbLoanType.setSelectedIndex(0); // select the JComboBox blank field
        
        // add the action listeners to the components
        cbLoanType.addItemListener(new cbLoanTypeItemListener());

        // add components to the panel using the GridBagLayout and GridBagConstraints
        //gc.anchor = GridBagConstraints.LINE_START;
        
        if (!mode.equals("prospect")) {
            gc.gridx = 0;
            gc.gridy = y;
            panel.add(new JLabel("Client : " + controller.getFirstName() + " " + controller.getLastName()), gc);
            y++;
        }

        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Type de prêt :"), gc);

        gc.gridx = 1;
        panel.add(cbLoanType, gc);
        y++;

        gc.gridx = 1;
        gc.gridy = y;
        if (mode.equals("prospect")) {
            btnBackToStart = new JButton("Retour");
            btnBackToStart.addActionListener(new BtnBackToStartListener());
            panel.add(btnBackToStart, gc);
        }
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    // display the fixed rate loan simulation form, adapted to the chosen loan type
    public void displayForm() {
        if(layoutChange) {
            panel.setLayout(new GridBagLayout());
            layoutChange = false;
        }
        
        // for the GBC gridy
        int y = 0;
        
        // hide the JPanel
        panel.setVisible(false);

        // remove components from the JPanel
        panel.removeAll();
        
        // initialisation of the new components
        txtFieldAmount = new JTextField(10);
        //txtFieldAmount.addActionListener(new ());
        txtFieldDuration = new JTextField(10);
        //txtFieldDuration.addActionListener(new ())
        
        txtFieldRate = new JTextField(5);
        btnSimulate = new JButton("Simuler");
        lblTotalRate = new JLabel("tauxtal");
        
        btnCancel = new JButton("Annuler");
        
        cbInsurance = new JComboBox();
        ArrayList<Insurance> insurancesList = controller.getInsurances(controller.getLoanType().getId());
        for (Insurance ins : insurancesList) {
            cbInsurance.addItem(ins);
        }
        
        // if it is a simulation from scratch
        if (mode.equals("simulate") || mode.equals("prospect")) {
            cbInsurance.insertItemAt("", 0); // add blank first item in JComboBox
            cbInsurance.setSelectedIndex(0); // select the JComboBox blank field
        }
        // if the simulation is based on another one, select the chosen insurance in the combobox
        else if (mode.equals("search/edit")) {
            ComboBoxModel cbInsuranceModel = cbInsurance.getModel();
            int size = cbInsuranceModel.getSize();
            int index = 0;
            Insurance ins;
            for(int i=0;i<size;i++) {
                System.out.println("id cb : " + ((Insurance)cbInsuranceModel.getElementAt(i)).getId());
                System.out.println("id modele : " + controller.getInsuranceId());
                System.out.println("index : " + i);
                System.out.println("");
                if (((Insurance)cbInsuranceModel.getElementAt(i)).getId() == controller.getInsuranceId()) {
                    index = i;
                }
            }
            System.out.println("");
            System.out.println("index : " + index);
            cbInsurance.getModel().setSelectedItem(cbInsurance.getModel().getElementAt(index));
        }
        else {
            controller.closeApplication("Affichage du formulaire de simulation : mode \"" + mode + "\" invalide");
        }
        
        // add the action listeners to the components
        btnSimulate.addActionListener(new BtnSimulateListener());

        // case of a simulation modification or a new simulation based on another one : fill the fields with the old simulation values
        if (mode.equals("search/edit") || editParams) {
            txtFieldRate.setText(String.valueOf(controller.getInterestRate()));
            txtFieldAmount.setText(String.valueOf(controller.getAmount()));
            txtFieldDuration.setText(String.valueOf(controller.getDuration()));
            editParams = false;
        }
            
        // add components to the panel using the GridBagLayout and GridBagConstraints
        //gc.anchor = GridBagConstraints.LINE_START;
        
        if (!mode.equals("prospect")) {
            gc.gridx = 0;
            gc.gridy = y;
            panel.add(new JLabel("Client : " + controller.getFirstName() + " " + controller.getLastName()), gc);
            y++;
        }

        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Type de prêt :"), gc);

        gc.gridx = 1;
        cbLoanType.setEnabled(false);
        panel.add(cbLoanType, gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Assurance :"), gc);

        gc.gridx = 1;
        panel.add(cbInsurance, gc);
        y++;

        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Taux d'intérêt de base :"), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(Double.toString(controller.getBaseRate()) + "%"), gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Taux d'intérêt :"), gc);
        
        gc.gridx = 1;
        panel.add(txtFieldRate, gc);
        y++;
        
        //gc.gridy = y;
        //gc.gridx = 0;
        //panel.add(new JLabel("TEG : "), gc);
        
        //gc.gridx = 1;
        //panel.add(lblTotalRate, gc);
        //y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Montant (" + controller.getMinAmount() + "€ - " + controller.getMaxAmount() + "€) :"), gc);
        
        gc.gridx = 1;
        panel.add(txtFieldAmount, gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Durée (" + controller.getMinLength() + " mois - " + controller.getMaxLength() + " mois) :"), gc);
        
        gc.gridx = 1;
        panel.add(txtFieldDuration, gc);
        y++;

        gc.gridy = y;
        gc.gridx = 0;
        panel.add(btnSimulate, gc);
        
        gc.gridx = 1;
        if (mode.equals("prospect")) {
            btnCancel.addActionListener(new BtnBackToStartListener());
        }
        else {
            btnCancel.addActionListener(new BtnBackToCustomerMenuListener());
        }
        panel.add(btnCancel, gc);
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    // display the simulation result
    public void displayResult() {
        // for the GBC gridy
        int y = 0;
        
        // hide the JPanel
        panel.setVisible(false);

        // remove components from the JPanel
        panel.removeAll();
        
        // initialisation of new components
        if (mode.equals("simulate")) {
            btnSave = new JButton("Sauvegarder");
            btnSave.addActionListener(new BtnsSaveListener());
        }
        else if (mode.equals("search/edit")) {
            btnUpdateSimulation = new JButton("Sauvegarder les modifications");
            btnSave = new JButton("Sauvegarder en tant que nouveau prêt");
            btnSave.addActionListener(new BtnsSaveListener());
            btnUpdateSimulation.addActionListener(new BtnUpdateListener());
        }
        else if (!mode.equals("prospect")) {
            controller.closeApplication("Affichage du résultat d'une simulation : mode \"" + mode + "\" invalide");
        }
        
        txtFieldWording = new JTextField(40);
        if (mode.equals("search/edit")) {
            txtFieldWording.setText(controller.getLoanWording());
        }
        
        // add components to the panel using the GridBagLayout and GridBagConstraints
        //gc.anchor = GridBagConstraints.LINE_START;
        
        if (!mode.equals("prospect")) {
            gc.gridx = 0;
            gc.gridy = y;
            panel.add(new JLabel("Client : " + controller.getFirstName() + " " + controller.getLastName()), gc);
            y++;
        }
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Type de prêt : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(controller.getLoanTypeWording()), gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Assurance : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(controller.getInsuranceWording()), gc);
        y++;

        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Montant : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(controller.getAmount() + " €"), gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Durée : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(controller.getDuration() + " mois"), gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Taux d'intérêt : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(controller.getInterestRate() + " %"), gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Taux assurance : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(controller.getInsuranceRate() + " %"), gc);
        y++;
        
        DecimalFormat df = new DecimalFormat("#.##"); // to format amounts display
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("TEG : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(df.format(controller.getTotalRate()) + " %"), gc);
        y++;

        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Montant des mensualités : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(df.format(controller.getMonthlyPayment()) + " €"), gc);
        y++;
        
        gc.gridy = y;
        gc.gridx = 0;
        panel.add(new JLabel("Montant total dû : "), gc);
        
        gc.gridx = 1;
        panel.add(new JLabel(df.format(controller.getOwedAmount()) + " €"), gc);
        y++;

        if (!mode.equals("prospect")) {
            gc.gridx = 0;
            gc.gridy = y;
            panel.add(new JLabel("Libellé du prêt :"), gc);
            y++;

            gc.gridwidth = 2;
            gc.gridx = 0;
            gc.gridy = y;
            panel.add(txtFieldWording, gc);
            gc.gridwidth = 1;
            y++;
            
            gc.gridx = 0;
            gc.gridy = y;
            if (mode.equals("simulate")) {
                panel.add(btnSave, gc);
                gc.gridx = 1;
                gc.gridy = y;
            }
            else if (mode.equals("search/edit")) {
                panel.add(btnSave,gc);
                gc.gridx = 1;
                gc.gridy = y;
                panel.add(btnUpdateSimulation,gc);
                gc.gridx = 2;
            }
            else if(!mode.equals("prospect")) {
                controller.closeApplication("Affichage du résultat d'une simulation : mode \"" + mode + "\" invalide");
            }
        }
        y++;
        gc.gridy = y;
        gc.gridx = 0;
        btnChangeParameters = new JButton("Modifier les valeurs");
        btnChangeParameters.addActionListener(new BtnChangeParametersListener());
        panel.add(btnChangeParameters);
        
        gc.gridx = 1;
        if (mode.equals("prospect")) {
            btnBackToStart = new JButton("Retour");
            btnBackToStart.addActionListener(new BtnBackToStartListener());
            panel.add(btnBackToStart, gc);
        }
        else {
            btnBackToCustomerMenu = new JButton("Retour");
            btnBackToCustomerMenu.addActionListener(new BtnBackToCustomerMenuListener());
            panel.add(btnBackToCustomerMenu,gc);
        }
        
        
        // perform the operations needed after the removal and the addition of components
        panel.revalidate();
        panel.repaint();
        
        // display the JPanel
        panel.setVisible(true);
    }
    
    public boolean checkJTableSelection() {
        return tblSimulations.getSelectedRowCount() == 1;
    }
    
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width +1 , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }
    
    public static void setUIFont (javax.swing.plaf.FontUIResource f){
    java.util.Enumeration keys = UIManager.getDefaults().keys();
    while (keys.hasMoreElements()) {
        Object key = keys.nextElement();
        Object value = UIManager.get (key);
        if (value != null && value instanceof javax.swing.plaf.FontUIResource)
            UIManager.put (key, f);
        }
    } 

    // ######################
    // inner listener classes
    // ######################
    
    class BtnChangeParametersListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            editParams = true;
            displayForm();
        }
    }
    
    class BtnBackToCustomerSearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.resetModel(true);
            displayCustomerSearch();
        }
    }
    
    class BtnBackToCustomerMenuListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.resetModel(false);
            displayMenu();
        }
    }
            
    class BtnBackToStartListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.resetModel(true);
            displayStartPage();
        }
    }
    
    // action listener for the btnProspect JButton
    class BtnProspectListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mode = "prospect";
            displayLoanTypes();
        }
    }
    
    // action listener for the btnCustomer JButton
    class BtnCustomerListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayCustomerSearch();
        }
    }
    
    // item listener for the btnSearchSimulation JButton
    class BtnSearchSimulationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mode = "search/edit";
            displayLoanTypes();
        }
    }
    
    // item listener for the btnCreateSimulation JButton
    class BtnCreateSimulationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            mode = "simulate";
            controller.resetModel(false);
            displayLoanTypes();
        }
    }
    
    // item listener for the cbLoanType JComboBox
    class cbLoanTypeItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            // perform action when an item from the JComboBox is selected
            if (e.getStateChange() == ItemEvent.SELECTED) {
                if (!cbLoanType.getSelectedItem().toString().isEmpty()) {
                    LoanType lt = (LoanType) cbLoanType.getSelectedItem();
                    // case when the user wants to display a customer's loans
                    if (mode.equals("search/edit")) {
                        controller.setLoanType(lt);
                        displayCustomerSimulations();
                    }
                    // case when the user wants to simulate a loan
                    else if (mode.equals("simulate") || mode.equals("prospect")) {
                        controller.setLoanType(lt);
                        displayForm();
                    }
                    else {
                        controller.closeApplication("Sélection de type de prêt : mode \"" + mode + "\" invalide");
                    }
                }
            }
        }
    }
    
    // item listener for the cbCustomer JComboBox
    class CbCustomerItemListener implements ItemListener {
        public void itemStateChanged(ItemEvent e) {
            // perform action when an item from the JComboBox is selected
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Customer c = (Customer) cbCustomer.getSelectedItem();
                controller.setCustomer(c);
                displayMenu();
            }
        }
    }

    // listener for btnSeach JButton
    class BtnSearchListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            displayCustomers();
        }
    }

    // listener for the btnSimulate JButton
    class BtnSimulateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String error = "Erreur :";
            boolean ok = true;
            
            if (cbInsurance.getSelectedItem().toString().isEmpty()) {
                ok = false;
                error += "\nVous devez sélectionner une assurance";
            }
            if(!txtFieldRate.getText().matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
                ok = false;
                error += "\nTaux incorrect";
            }
            if(txtFieldAmount.getText().matches("\\d+")) {
                if (Integer.parseInt(txtFieldAmount.getText()) < controller.getMinAmount()) {
                    ok = false;
                    error += "\nMontant trop petit";
                }
                else if (Integer.parseInt(txtFieldAmount.getText()) > controller.getMaxAmount()) {
                    ok = false;
                    error += "\nMontant trop élevé";
                }
            }
            else {
                ok = false;
                error += "\nMontant incorrect";
            }
            if(txtFieldDuration.getText().matches("\\d+")) {
                if (Integer.parseInt(txtFieldDuration.getText()) < controller.getMinLength()) {
                    ok = false;
                    error += "\nDurée trop courte";
                }
                else if (Integer.parseInt(txtFieldDuration.getText()) > controller.getMaxLength()) {
                    ok = false;
                    error += "\nDurée trop élevée";
                }
            }
            else {
                ok = false;
                error += "\nDurée incorrecte";
            }
            
            if (ok) {
                controller.setInsurance((Insurance)cbInsurance.getSelectedItem());
                int duration, amount;
                controller.setInterestRate(Double.parseDouble(txtFieldRate.getText()));
                duration = Integer.parseInt(txtFieldDuration.getText());
                controller.setDuration(duration);
                amount = Integer.parseInt(txtFieldAmount.getText());
                controller.setAmount(amount);

                controller.calculateLoan();
                displayResult();
            }
            else {
                JOptionPane.showMessageDialog(null,error);
            }
        }
    }
    
    // listener for btnUpdateSimulation
    class BtnUpdateListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.setLoanWording(txtFieldWording.getText());
            if (controller.updateLoanSimulation()) {
                JOptionPane.showMessageDialog(panel, "Le prêt a été mis à jour", "Succès", JOptionPane.INFORMATION_MESSAGE);
                btnUpdateSimulation.setEnabled(false);
            }
            else {
                JOptionPane.showMessageDialog(panel, "Echec de la mise à jour du prêt", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // listener for btnSave JButton
    class BtnsSaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            controller.setLoanWording(txtFieldWording.getText());
            if (controller.saveLoanSimulation()) {
                JOptionPane.showMessageDialog(panel, "Le prêt a été sauvegardé", "Succès", JOptionPane.INFORMATION_MESSAGE);
                btnSave.setEnabled(false);
            }
            else {
                JOptionPane.showMessageDialog(panel, "Echec de la sauvegarde du prêt", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // listener for the btnEditSimulation
    class BtnEditSimulationListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (checkJTableSelection()) {
                controller.selectSimulation((Integer)mdlSimulations.getValueAt(tblSimulations.getSelectedRow(),0));
                displayForm();
            }
            else {
                JOptionPane.showMessageDialog(null,"Vous devez sélectionner une et une seule simulation");
            }
        }
         
    }
}
