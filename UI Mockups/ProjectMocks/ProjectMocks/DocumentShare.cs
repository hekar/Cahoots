using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;

namespace ProjectMocks
{
    public partial class DocumentShare : Form
    {
        public DocumentShare()
        {
            InitializeComponent();
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void DocumentShare_Load(object sender, EventArgs e)
        {
            this.Text = "Share Document(s)";
        }
    }
}
