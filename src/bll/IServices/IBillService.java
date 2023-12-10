package bll.IServices;

import bll.dto.BillDTO;

public interface IBillService extends IService<BillDTO> {
	int count();
}
