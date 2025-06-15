import { useEffect, useState } from "react";
import API_BASE_URL from "../../global/components/config";
import {
  Form,
  Input,
  Button,
  message,
  InputNumber,
  Select,
  Checkbox,
  Radio,
} from "antd";

const FormRenderer = ({ schema, onClose, refreshTableData, schemaId }) => {
  const [isButtonDisabled, setIsButtonDisabled] = useState(true);
  const [form] = Form.useForm();
  const [messageApi, contextHolder] = message.useMessage();
  const [currentSchemaId, setCurrentSchemaId] = useState(schemaId);
  const handleFieldChange = () => {
    const haveError = form
      .getFieldError()
      .some(({ errors }) => errors.length > 0);
    const touchedAllField = form.isFieldsTouched(true);
    setIsButtonDisabled(haveError || !touchedAllField);
  };

  const getValidationRules = (fieldSchema) => {
    const rules = [];

    if (fieldSchema.required) {
      rules.push({
        required: true,
        message: "This is a required field",
      });
    }

    if (fieldSchema.type === "string") {
      if (fieldSchema.minLength) {
        rules.push({
          min: fieldSchema.minLength,
          message: `value should be of minimum length ${fieldSchema.minLength}`,
        });
      }

      if (fieldSchema.maxLength) {
        rules.push({
          max: fieldSchema.maxLength,
          message: `value should not exceed maximum length ${fieldSchema.minLength}`,
        });
      }

      if (fieldSchema.pattern) {
        rules.push({
          pattern: new RegExp(fieldSchema.pattern),
          message: fieldSchema.patternMessage || "Invalid format",
        });
      }

      if (fieldSchema.format === "email") {
        rules.push({
          type: "email",
          message: "Please enter a valid email address",
        });
      }
    }

    if (fieldSchema.type === "number" || fieldSchema.type === "integer") {
      rules.push({
        validator: (_, value) => {
          if (value === undefined || value === "") return Promise.resolve();
          const num = Number(value);
          if (isNaN(num)) {
            return Promise.reject("value must be a number");
          }
          return Promise.resolve();
        },
      });

      if (fieldSchema.minValue !== undefined) {
        rules.push({
          type: "number",
          min: fieldSchema.minValue,
          message: `value must be higher than the minimum limit ${fieldSchema.minValue}`,
        });
      }

      if (fieldSchema.maxValue !== undefined) {
        rules.push({
          type: "number",
          max: fieldSchema.maxValue,
          message: `value should not exceed the maximum limit ${fieldSchema.maxValue}`,
        });
      }
    }

    return rules;
  };

  const handleFinish = async (value) => {
    try {
      let schemaIdToUse = currentSchemaId;
      if (!schemaIdToUse) {
        const response = await fetch(`${API_BASE_URL}/formbuilder/saveSchema`, {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            schema: schema,
          }),
        });

        if (!response.ok) {
          throw new Error(`HTTP error - status: ${response.status}`);
        }
        const newSchemaId = await response.text();
        setCurrentSchemaId(newSchemaId);
        schemaIdToUse = newSchemaId;
      }
      const res = await fetch(`${API_BASE_URL}/formbuilder/addFormData`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          schemaId: schemaIdToUse,
          formData: value,
        }),
      });

      if (!res.ok) {
        throw new Error(`HTTP error - status: ${res.status}`);
      }

      if (onClose) {
        onClose();
      }
      if (refreshTableData) {
        console.log("data refresh");
        refreshTableData();
      }
      form.resetFields();
      messageApi.success("Form Added");
    } catch (error) {
      messageApi.error("Server Error");
      console.error("Submit failed:", error);
    }
  };
  const getInputField = (key, fieldSchema) => {
    const placeholder = fieldSchema.description || `Enter ${key}`;

    if (fieldSchema.enum) {
      const options = fieldSchema.enum.map((val, idx) => ({
        value: val,
        label: fieldSchema.enumNames ? fieldSchema.enumNames[idx] : val,
      }));

      if (fieldSchema["widget"] === "radio") {
        return <Radio.Group options={options} />;
      }
      return (
        <Select placeholder={`Select ${key}`}>
          {options.map((opt) => (
            <Select.Option key={opt.value} value={opt.value}>
              {opt.label}
            </Select.Option>
          ))}
        </Select>
      );
    }

    switch (fieldSchema.type) {
      case "string":
        return <Input placeholder={placeholder} />;

      case "number":
      case "integer":
        return (
          <InputNumber style={{ width: "100%" }} placeholder={placeholder} />
        );

      case "boolean":
        return <Checkbox>{fieldSchema.title || key}</Checkbox>;

      default:
        return <Input placeholder={placeholder} />;
    }
  };

  return (
    <>
      {contextHolder}
      <div className={style.container}>
        <Form
          form={form}
          layout="vertical"
          onFinish={handleFinish}
          onFieldsChange={handleFieldChange}
        >
          {Object.entries(schema.properties).map(([key, fieldSchema]) => {
            const isCheckbox = fieldSchema.type === "boolean";
            return (
              <Form.Item
                key={key}
                name={key}
                label={key}
                validateTrigger="onChange"
                rules={getValidationRules(fieldSchema)}
                valuePropName={isCheckbox ? "checked" : "value"}
              >
                {getInputField(key, fieldSchema)}
              </Form.Item>
            );
          })}
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              disabled={isButtonDisabled}
            >
              Submit
            </Button>
          </Form.Item>
        </Form>
      </div>
    </>
  );
};

export default FormRenderer;
