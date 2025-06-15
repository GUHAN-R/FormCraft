import { Table, Button, message } from "antd";
import { useEffect, useState } from "react";
import API_BASE_URL from "../../global/components/config";
import { DownloadOutlined } from "@ant-design/icons";

const HistoryTable = ({
  dataSource,
  setIsModalOpen,
  setSchema,
  setSchemaId,
}) => {
  const [messageApi, contextHolder] = message.useMessage();

  const handleAddRecord = async (record) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/formbuilder/getSchema?schemaId=${record.schemaId}`,
        {
          method: "GET",
        }
      );
      if (!response.ok) {
        throw new Error("Failed to fetch schema");
      }

      const schemaData = await response.json();
      console.log(schemaData);
      setSchema(schemaData.jsonSchema);
      setSchemaId(record.schemaId);
      setIsModalOpen(true);
      messageApi.success("Schema loaded successfully");
    } catch (error) {
      messageApi.error("Error While loading schema");
      console.error("Error loading schema:", error);
    }
  };
  const handleClick = async (record) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/formbuilder/export?schemaId=${record.schemaId}`
      );
      if (!response.ok) {
        throw new Error("Failed to Export");
      }
      const data = await response.json();

      const blob = new Blob([JSON.stringify(data, null, 2)], {
        type: "application/json",
      });
      const url = URL.createObjectURL(blob);

      const a = document.createElement("a");
      a.href = url;
      a.download = "form.json";
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);

      URL.revokeObjectURL(url);
      messageApi.success("Successfully Exported");
    } catch (error) {
      messageApi.error("Export Failed");
      console.error("Error in Exporting", error);
    }
  };

  const columns = [
    {
      title: "S.No",
      dataIndex: "key",
      key: "sno",
      render: (text, record, index) => index + 1,
    },
    {
      title: "Schema ID",
      dataIndex: "schemaId",
      key: "schemaId",
    },
    {
      title: "Schema Title",
      dataIndex: "title",
      key: "title",
    },
    {
      title: "Schema Description",
      dataIndex: "description",
      key: "description",
    },
    {
      title: "Action",
      key: "action0",
      render: (_, record) => (
        <Button type="primary" onClick={() => handleAddRecord(record)}>
          Add Form
        </Button>
      ),
    },

    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Button
          type="primary"
          style={{ backgroundColor: "green" }}
          onClick={() => handleClick(record)}
          icon={<DownloadOutlined />}
        >
          Export
        </Button>
      ),
    },
  ];
  return (
    <>
      {contextHolder}
      <div style={{ width: "75%" }}>
        <Table dataSource={dataSource} columns={columns} pagination={false} />
      </div>
    </>
  );
};
export default HistoryTable;
