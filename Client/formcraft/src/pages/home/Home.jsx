import { Upload, Button, message, Modal } from "antd";
import { PlusOutlined } from "@ant-design/icons";
import style from "./scss/Home.module.scss";
import HistoryTable from "./HistoryTable";
import { useState, useEffect } from "react";
import FormRenderer from "./FormRenderer";
import API_BASE_URL from "../../global/components/config";

const Home = () => {
  const [schema, setSchema] = useState(null);
  const [schemaId, setSchemaId] = useState(null);
  const [dataSource, setDataSource] = useState([]);
  const [messageApi, contextHolder] = message.useMessage();
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [refresh, setRefresh] = useState(0);

  const refreshTableData = () => {
    console.log("cbwjkcwc");
    setRefresh((prev) => prev + 1);
  };
  useEffect(() => {
    fetchData();
  }, [refresh]);

  const fetchData = async () => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/formbuilder/getSchemas?userId=1`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error(`HTTP error - status: ${response.status}`);
      }
      const data = await response.json();

      const transformedData = data.map((item) => ({
        key: item.schemaId,
        schemaId: item.schemaId,
        title: item.title,
        description: item.description,
      }));

      setDataSource(transformedData);
    } catch (error) {
      messageApi.error("Server Error ");
      console.error("data History fetching Failed", error.message);
    }
  };

  const handleUpload = (file) => {
    const fileReader = new FileReader();

    fileReader.onload = (event) => {
      try {
        const parsedContent = JSON.parse(event.target.result);
        setSchema(parsedContent);
        console.log(parsedContent);
        setIsModalOpen(true);
      } catch (error) {
        messageApi.error("Invalid JSON file");
        setSchema(null);
      }
    };

    fileReader.readAsText(file);
    return false;
  };

  const handleCancel = () => {
    setIsModalOpen(false);
  };

  return (
    <>
      {contextHolder}
      <div className={style.container}>
        <div className={style.generateButton}>
          <div className={style.fileUploadSection}>
            <Upload
              beforeUpload={handleUpload}
              accept=".json"
              showUploadList={false}
            >
              <Button icon={<PlusOutlined />} type="primary">
                Upload schema
              </Button>
            </Upload>
          </div>
        </div>
        <div className={style.historySection}>
          <div className={style.title}>
            <p className={style.text}>Form History Table</p>
          </div>
          <div className={style.table}>
            <HistoryTable
              dataSource={dataSource}
              setIsModalOpen={setIsModalOpen}
              setSchema={setSchema}
              setSchemaId={setSchemaId}
            />
          </div>
        </div>
      </div>
      <div>
        <Modal
          title={schema?.title || "Form"}
          closable={{ "aria-label": "Custom Close Button" }}
          open={isModalOpen}
          onCancel={handleCancel}
          footer={null}
          destroyOnHidden
        >
          <FormRenderer
            schema={schema}
            onClose={handleCancel}
            refreshTableData={refreshTableData}
            schemaId={schemaId}
          />
        </Modal>
      </div>
    </>
  );
};

export default Home;
